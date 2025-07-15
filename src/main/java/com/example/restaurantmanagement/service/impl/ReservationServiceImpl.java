package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.dto.ReservationTimeStatDTO;
import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final TableInfoRepository tableInfoRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, CustomerRepository customerRepository, TableInfoRepository  tableInfoRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.tableInfoRepository = tableInfoRepository;
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        if (reservation.getId() != null) {
            throw new NVException(ErrorCode.RESERVATION_ID_NOT_NULL);
        }

        Integer customerId = reservation.getCustomer().getId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NVException(ErrorCode.CUSTOMER_NOT_FOUND));
        reservation.setCustomer(customer);

        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();

        if (start == null || end == null) {
            throw new NVException(ErrorCode. TIME_START_END_NOT_NULL);

        }

        List<TableInfo> fullTables = new ArrayList<>();

        for (TableInfo t : reservation.getTableInfos()) {
            Integer tableId = t.getId();

            // Lấy bàn gốc từ DB
            TableInfo table = tableInfoRepository.findById(tableId)
                    .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));

            // Kiểm tra conflict
            List<TableInfo> conflicts = tableInfoRepository.findConflicts(table.getTableNumber(), start, end);
            if (!conflicts.isEmpty()) {
                throw new NVException(ErrorCode.TABLE_CONFLICT, new Object[]{table.getTableNumber()});
            }

            table.setStatus(TableInfo.TableStatus.RESERVED);
            fullTables.add(table);
        }

        // Gán danh sách đã chuẩn bị vào Reservation
        reservation.setTableInfos(fullTables);

        // Hibernate sẽ tự gán reservation_id cho tableInfo nhờ cascade
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Reservation updatedReservation) {
        if (updatedReservation.getId() == null) {
            throw new NVException(ErrorCode.RESERVATION_ID_NOT_NULL);
        }

        Optional<Reservation> existingOpt = reservationRepository.findById(updatedReservation.getId());
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        Reservation existing = existingOpt.get();
        existing.setCustomer(updatedReservation.getCustomer());
        existing.setTableInfos(updatedReservation.getTableInfos());
        existing.setStartTime(updatedReservation.getStartTime());
        existing.setEndTime(updatedReservation.getEndTime());
        existing.setNote(updatedReservation.getNote());
        existing.setNumberOfPeople(updatedReservation.getNumberOfPeople());
        existing.setStatus(updatedReservation.getStatus());

        return reservationRepository.save(existing);
    }
    @Override
    public List<ReservationTimeStatDTO> countReservationsByDayAndHour() {
        List<Reservation> reservations = reservationRepository.findAll();

        Map<String, Map<Integer, Long>> grouped = reservations.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getStartTime().getDayOfWeek().toString(),
                        Collectors.groupingBy(
                                r -> r.getStartTime().getHour(),
                                Collectors.counting()
                        )
                ));

        List<ReservationTimeStatDTO> result = new ArrayList<>();

        grouped.forEach((day, hourMap) -> {
            hourMap.forEach((hour, count) -> {
                result.add(new ReservationTimeStatDTO(day, hour, count));
            });
        });

        return result;
    }

    @Override
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
    @Override
    public void deleteReservation(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new NVException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteById(id);
    }

}
