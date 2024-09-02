import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import axios from 'axios';
import HomePage from './HomePage';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

const renderHomePage = () => {
  render(
    <BrowserRouter>
      <HomePage />
    </BrowserRouter>
  );
};

describe('HomePage', () => {
  beforeEach(() => {
    mockedAxios.get.mockResolvedValue({ data: [{ code: 'TEST', cityName: 'Test City', name: 'Test Airport' }] });
    mockedAxios.post.mockResolvedValue({ data: [] });
  });

  test('calls API and navigates when all required fields are filled', async () => {
    renderHomePage();

    fireEvent.change(screen.getByLabelText(/Departure Airport/i), { target: { value: 'TEST' } });
    fireEvent.change(screen.getByLabelText(/Arrival Airport/i), { target: { value: 'TEST' } });
    fireEvent.change(screen.getByLabelText(/Departure Date/i), { target: { value: '2023-05-01' } });
    fireEvent.change(screen.getByLabelText(/Passengers/i), { target: { value: '1' } });

    fireEvent.click(screen.getByText(/Search Flights/i));

    await waitFor(() => {
      expect(mockedAxios.post).toHaveBeenCalledWith('http://localhost:8080/flights/search', expect.any(Object));
      expect(mockNavigate).toHaveBeenCalledWith('/results', expect.any(Object));
    });
  });

  test('shows error modal when API call fails', async () => {
    mockedAxios.post.mockRejectedValue(new Error('API Error'));
    renderHomePage();

    fireEvent.change(screen.getByLabelText(/Departure Airport/i), { target: { value: 'TEST' } });
    fireEvent.change(screen.getByLabelText(/Arrival Airport/i), { target: { value: 'TEST' } });
    fireEvent.change(screen.getByLabelText(/Departure Date/i), { target: { value: '2023-05-01' } });
    fireEvent.change(screen.getByLabelText(/Passengers/i), { target: { value: '1' } });

    fireEvent.click(screen.getByText(/Search Flights/i));

    await waitFor(() => {
      expect(screen.getByText(/Error/i)).toBeInTheDocument();
      expect(screen.getByText(/An unexpected error occurred while searching for flights./i)).toBeInTheDocument();
    });
  });
});