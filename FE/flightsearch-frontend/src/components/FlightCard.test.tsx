import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import FlightCard from './FlightCard';

const mockFlight = {
    awayDepartureTime: '10:00',
    awayArrivalTime: '12:00',
    airline: 'Test Airline',
    awayDuration: '2h',
    priceBreakdown: {
        total: 200,
        pricePerTraveler: 100
    },
    returnSegments: []
};

const mockProps = {
    flight: mockFlight,
    departureCity: 'New York',
    departureAirport: 'JFK',
    arrivalCity: 'Los Angeles',
    arrivalAirport: 'LAX',
    currency: 'USD',
    adults: 2
};

describe('FlightCard', () => {
    test('renders flight information correctly', () => {
        render(<FlightCard {...mockProps} />);

        expect(screen.getByText('New York (JFK) - Los Angeles (LAX)')).toBeInTheDocument();
        expect(screen.getByText('10:00 - 12:00')).toBeInTheDocument();
        expect(screen.getByText('Airline: Test Airline')).toBeInTheDocument();
        expect(screen.getByText('Duration: 2h')).toBeInTheDocument();
        expect(screen.getByText('200 USD')).toBeInTheDocument();
        expect(screen.getByText('100 USD')).toBeInTheDocument();
    });

    test('opens FlightDetailsModal when clicked', () => {
        render(<FlightCard {...mockProps} />);

        fireEvent.click(screen.getByText('New York (JFK) - Los Angeles (LAX)'));

        expect(screen.getByText('Flight Details')).toBeInTheDocument();
    });

    test('renders return flight information when available', () => {
        const flightWithReturn = {
            ...mockFlight,
            returnSegments: [
                {
                    departureTime: '14:00',
                    arrivalTime: '16:00',
                    airline: 'Return Airline',
                    duration: '2h'
                }
            ]
        };

        render(<FlightCard {...mockProps} flight={flightWithReturn} />);

        expect(screen.getByText('Los Angeles (LAX) - New York (JFK)')).toBeInTheDocument();
        expect(screen.getByText('14:00 - 16:00')).toBeInTheDocument();
        expect(screen.getByText('Airline: Return Airline')).toBeInTheDocument();
    });
});