package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // ✅ CREATE
    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        if (reservation.getId() != null) {
            throw new IllegalArgumentException("New reservation must not have an ID");
        }

        Integer customerId = reservation.getCustomer().getId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
        reservation.setCustomer(customer);

        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        List<TableInfo> fullTables = new ArrayList<>();

        for (TableInfo t : reservation.getTableInfos()) {
            Integer tableId = t.getId();

            // 👉 Lấy bàn gốc từ DB
            TableInfo table = tableInfoRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("TableInfo not found with id: " + tableId));

            // Kiểm tra conflict
            List<TableInfo> conflicts = tableInfoRepository.findConflicts(table.getTableNumber(), start, end);
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException("Bàn " + table.getTableNumber() + " đã được đặt trong thời gian này");
            }

            // 👉 KHÔNG GÁN reservation ở đây nữa
            table.setStatus(TableInfo.TableStatus.RESERVED);
            fullTables.add(table);
        }

        // Gán danh sách đã chuẩn bị vào Reservation
        reservation.setTableInfos(fullTables);

        // 👉 Hibernate sẽ tự gán reservation_id cho tableInfo nhờ cascade
        return reservationRepository.save(reservation);
    }


    // ✅ UPDATE
    @Override
    public Reservation updateReservation(Reservation updatedReservation) {
        if (updatedReservation.getId() == null) {
            throw new IllegalArgumentException("Reservation ID must not be null");
        }

        Optional<Reservation> existingOpt = reservationRepository.findById(updatedReservation.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Reservation not found with ID = " + updatedReservation.getId());
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

    // ✅ READ ONE
    @Override
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    // ✅ READ ALL
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // ✅ DELETE
    @Override
    public void deleteReservation(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found with ID = " + id);
        }
        reservationRepository.deleteById(id);
    }
}
