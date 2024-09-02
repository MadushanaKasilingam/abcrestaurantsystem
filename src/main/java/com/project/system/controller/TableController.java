package com.project.system.controller;

import com.project.system.model.DineinTable;
import com.project.system.response.MessageResponse;
import com.project.system.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping
    public ResponseEntity<?> createDineinTable(@RequestBody DineinTable dineinTable) {
        try {
            DineinTable createdTable = tableService.createDineinTable(dineinTable);
            return new ResponseEntity<>(new MessageResponse("Table added successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DineinTable> getTableById(@PathVariable Long id) throws Exception {
        DineinTable dineinTable = tableService.findTableById(id);
        return new ResponseEntity<>(dineinTable, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DineinTable>> getAllTables() {
        List<DineinTable> dineinTables = tableService.getAllTables();
        return new ResponseEntity<>(dineinTables, HttpStatus.OK);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateTableAvailability(
            @PathVariable Long id, @RequestBody Map<String, Boolean> availabilityRequest
    ) {
        try {
            boolean isAvailable = availabilityRequest.get("isAvailable");
            tableService.updateTableAvailability(id, isAvailable);
            return new ResponseEntity<>(new MessageResponse("Table availability updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        try {
            tableService.deleteTable(id);
            return new ResponseEntity<>(new MessageResponse("Table deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/by-restaurant")
    public ResponseEntity<List<DineinTable>> getTablesByRestaurantName(@RequestParam String restaurantName) {
        List<DineinTable> dineinTables = tableService.findTablesByRestaurantName(restaurantName);
        return new ResponseEntity<>(dineinTables, HttpStatus.OK);
    }

    // Add this method to TableController.java

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDineinTable(
            @PathVariable Long id, @RequestBody DineinTable dineinTable
    ) {
        try {
            dineinTable.setId(id); // Ensure the ID from the path is set on the table object
            DineinTable updatedTable = tableService.updateDineinTable(dineinTable);
            return new ResponseEntity<>(updatedTable, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }}


    }