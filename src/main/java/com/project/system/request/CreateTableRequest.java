package com.project.system.request;

import jakarta.validation.constraints.NotNull;

public class CreateTableRequest {

    @NotNull(message = "Restaurant ID is mandatory")
    private Long restaurantId;

    @NotNull(message = "Table Number is mandatory")
    private Integer tableNumber;

    @NotNull(message = "Seats are mandatory")
    private Integer seats;

    @NotNull(message = "Availability is mandatory")
    private Boolean available;

    public @NotNull(message = "Restaurant ID is mandatory") Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(@NotNull(message = "Restaurant ID is mandatory") Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public @NotNull(message = "Table Number is mandatory") Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(@NotNull(message = "Table Number is mandatory") Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public @NotNull(message = "Seats are mandatory") Integer getSeats() {
        return seats;
    }

    public void setSeats(@NotNull(message = "Seats are mandatory") Integer seats) {
        this.seats = seats;
    }

    public @NotNull(message = "Availability is mandatory") Boolean getAvailable() {
        return available;
    }

    public void setAvailable(@NotNull(message = "Availability is mandatory") Boolean available) {
        this.available = available;
    }
}