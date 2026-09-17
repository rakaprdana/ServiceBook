# Implementation Plan - Search Customer Feature

This plan outlines the steps to implement a search feature based on the customer's name in the `MainActivity` (Dashboard).

## User Review Required

> [!IMPORTANT]
> The search will be case-insensitive and will filter the list in real-time as the user types.

## Proposed Changes

### Dashboard Component

#### [MODIFY] [MainActivity.kt](file:///D:/android-studio/ServiceBook/app/src/main/java/com/example/servicebook/MainActivity.kt)
- Add a search listener to `bindingDashboard.etCariPelanggan`.
- Update the filtering logic to combine both product category filter and search query.
- Implement `setSearchFunction()` to handle the search input.

## Verification Plan

### Manual Verification
- Deploy the app to the device/emulator.
- Type a customer name in the search bar.
- Verify that the list updates correctly based on the input.
- Change the product category and verify that the search filter is still applied correctly (and vice versa).
