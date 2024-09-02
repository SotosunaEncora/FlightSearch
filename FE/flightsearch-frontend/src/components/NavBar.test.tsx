import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import NavBar from './NavBar';

const renderNavBar = () => {
  render(
    <BrowserRouter>
      <NavBar />
    </BrowserRouter>
  );
};

describe('NavBar', () => {
  test('renders the NavBar component', () => {
    renderNavBar();
    const appBarElement = screen.getByRole('banner');
    expect(appBarElement).toBeInTheDocument();
  });

  test('displays the correct title', () => {
    renderNavBar();
    const titleElement = screen.getByText('Flight Search');
    expect(titleElement).toBeInTheDocument();
  });

  test('title links to home page', () => {
    renderNavBar();
    const linkElement = screen.getByRole('link', { name: 'Flight Search' });
    expect(linkElement).toHaveAttribute('href', '/');
  });

  test('uses MUI AppBar and Toolbar', () => {
    renderNavBar();
    const appBarElement = screen.getByRole('banner');
    expect(appBarElement).toHaveClass('MuiAppBar-root');
    const toolbarElement = appBarElement.querySelector('.MuiToolbar-root');
    expect(toolbarElement).toBeInTheDocument();
  });
});