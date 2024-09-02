import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage';
import SearchResultsPage from './components/FlightsResultsPage';
import Navbar from './components/NavBar';

const App: React.FC = () => {
  return (
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/results" element={<SearchResultsPage />} />
        </Routes>
      </Router>
  );
}

export default App;
