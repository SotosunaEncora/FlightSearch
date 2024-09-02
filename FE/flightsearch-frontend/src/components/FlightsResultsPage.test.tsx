import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import FlightsResultsPage from './FlightsResultsPage';

// Mock the FlightCard component
jest.mock('./FlightCard', () => {
  return function MockFlightCard(props: any) {
    return <div data-testid="flight-card">{JSON.stringify(props)}</div>;
  };
});

const mockState = {
  flights: [
    { id: 1, someFlightData: 'data1' },
    { id: 2, someFlightData: 'data2' },
  ],
  departureAirport: { cityName: 'New York', code: 'JFK' },
  arrivalAirport: { cityName: 'Los Angeles', code: 'LAX' },
  departureDate: '2023-07-01',
  returnDate: '2023-07-08',
  adults: 2,
  currency: 'USD',
  nonStop: false,
};

const renderWithRouter = (component: React.ReactNode, state = mockState) => {
  return render(
    <MemoryRouter initialEntries={[{ pathname: '/results', state }]}>
      <Routes>
        <Route path="/results" element={component} />
      </Routes>
    </MemoryRouter>
  );
};

describe('FlightsResultsPage', () => {
  test('renders Flight Results title', () => {
    renderWithRouter(<FlightsResultsPage />);
    expect(screen.getByText('Flight Results')).toBeInTheDocument();
  });

  test('renders correct number of FlightCards', () => {
    renderWithRouter(<FlightsResultsPage />);
    const flightCards = screen.getAllByTestId('flight-card');
    expect(flightCards).toHaveLength(2);
  });

  test('passes correct props to FlightCard', () => {
    renderWithRouter(<FlightsResultsPage />);
    const flightCards = screen.getAllByTestId('flight-card');
    const firstCardProps = JSON.parse(flightCards[0].textContent || '');
    
    expect(firstCardProps.flight).toEqual(mockState.flights[0]);
    expect(firstCardProps.departureCity).toBe('New York');
    expect(firstCardProps.departureAirport).toBe('JFK');
    expect(firstCardProps.arrivalCity).toBe('Los Angeles');
    expect(firstCardProps.arrivalAirport).toBe('LAX');
    expect(firstCardProps.currency).toBe('USD');
    expect(firstCardProps.adults).toBe(2);
  });

  test('handles empty flights array', () => {
    const emptyState = { ...mockState, flights: [] };
    renderWithRouter(<FlightsResultsPage />, emptyState);
    const flightCards = screen.queryAllByTestId('flight-card');
    expect(flightCards).toHaveLength(0);
  });
});