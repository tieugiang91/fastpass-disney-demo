package com.upwork.ticketmanagement.application.dto;

public record IssueTicketRequest(String attractionId, String attractionName, int waitTimeMinutes) {
}