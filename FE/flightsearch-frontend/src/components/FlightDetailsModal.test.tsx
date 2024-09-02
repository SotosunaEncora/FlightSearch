import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import FlightDetailsModal from './FlightDetailsModal';

const mockFlight = {
    departureSegments: [
        {
            origin: 'JFK',
            destination: 'LAX',
            departureTime: '10:00',
            arrivalTime: '13:00',
            airline: 'Test Airline',
            flightNumber: 'TA123',
            aircraft: 'Boeing 737',
            duration: '3h',
            details: {
                fareCabin: 'Economy',
                fareClass: 'Y',
                fareBasis: 'Y123',
                checkedBags: ['1 piece']
            }
        }
    ],
    returnSegments: [
        {
            origin: 'LAX',
            destination: 'JFK',
            departureTime: '15:00',
            arrivalTime: '23:00',
            airline: 'Test Airline',
            flightNumber: 'TA456',
            aircraft: 'Airbus A320',
            duration: '5h',
            details: {
                fareCabin: 'Economy',
                fareClass: 'Y',
                fareBasis: 'Y456',
                checkedBags: ['1 piece']
            }
        }
    ],
    priceBreakdown: {
        base: 200,
        total: 250,
        fees: 50,
        pricePerTraveler: 250,
        currency: 'USD'
    }
};

describe('FlightDetailsModal', () => {
    test('renders modal when open is true', () => {
        render(<FlightDetailsModal open={true} onClose={() => {}} flight={mockFlight} />);
        expect(screen.getByText('Flight Details')).toBeInTheDocument();
    });

    test('does not render modal when open is false', () => {
        render(<FlightDetailsModal open={false} onClose={() => {}} flight={mockFlight} />);
        expect(screen.queryByText('Flight Details')).not.toBeInTheDocument();
    });

    test('renders departure segment details', () => {
        render(<FlightDetailsModal open={true} onClose={() => {}} flight={mockFlight} />);
        expect(screen.getByText('JFK to LAX')).toBeInTheDocument();
        expect(screen.getByText('Departure: 10:00')).toBeInTheDocument();
        expect(screen.getByText('Arrival: 13:00')).toBeInTheDocument();
        expect(screen.getAllByText('Airline: Test Airline')[0]).toBeInTheDocument();
        expect(screen.getByText('Flight Number: TA123')).toBeInTheDocument();
        expect(screen.getByText('Aircraft: Boeing 737')).toBeInTheDocument();
        expect(screen.getByText('Duration: 3h')).toBeInTheDocument();
    });

    test('renders return segment details', () => {
        render(<FlightDetailsModal open={true} onClose={() => {}} flight={mockFlight} />);
        expect(screen.getByText('LAX to JFK')).toBeInTheDocument();
        expect(screen.getByText('Departure: 15:00')).toBeInTheDocument();
        expect(screen.getByText('Arrival: 23:00')).toBeInTheDocument();
        expect(screen.getByText('Flight Number: TA456')).toBeInTheDocument();
        expect(screen.getByText('Aircraft: Airbus A320')).toBeInTheDocument();
        expect(screen.getByText('Duration: 5h')).toBeInTheDocument();
    });

    test('renders price breakdown', () => {
        render(<FlightDetailsModal open={true} onClose={() => {}} flight={mockFlight} />);
        expect(screen.getByText('Base Price: 200 USD')).toBeInTheDocument();
        expect(screen.getByText('Total Price: 250 USD')).toBeInTheDocument();
        expect(screen.getByText('Fees: 50 USD')).toBeInTheDocument();
        expect(screen.getByText('Price per Traveler: 250 USD')).toBeInTheDocument();
    });

    test('calls onClose when close button is clicked', () => {
        const mockOnClose = jest.fn();
        render(<FlightDetailsModal open={true} onClose={mockOnClose} flight={mockFlight} />);
        fireEvent.click(screen.getByText('Close'));
        expect(mockOnClose).toHaveBeenCalled();
    });
});