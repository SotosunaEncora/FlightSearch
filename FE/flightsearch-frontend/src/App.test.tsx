import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Flight Search title', () => {
  render(<App />);
  const titleElement = screen.getByText(/Flight Search/i);
  expect(titleElement).toBeInTheDocument();
});
