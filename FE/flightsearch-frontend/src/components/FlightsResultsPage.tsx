import React from 'react';
import { useLocation } from 'react-router-dom';
import { Container, Box, Typography } from '@mui/material';
import FlightCard from './FlightCard';

const FlightsResultsPage: React.FC = () => {
    const location = useLocation();
    const { flights, departureAirport, arrivalAirport, departureDate, returnDate, adults, currency, nonStop } = location.state;

    return (
        <Container
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                paddingTop: '2rem',
                paddingBottom: '2rem',
            }}
        >
            <Typography variant="h4" gutterBottom>
                Flight Results
            </Typography>
            <Box sx={{ width: '100%', maxWidth: 800 }}>
                {flights.map((flight: any, index: number) => (
                    <FlightCard
                        key={index}
                        flight={flight}
                        departureCity={departureAirport.cityName}
                        departureAirport={departureAirport.code}
                        arrivalCity={arrivalAirport.cityName}
                        arrivalAirport={arrivalAirport.code}
                        currency={currency}
                        adults={adults}
                    />
                ))}
            </Box>
        </Container>
    );
};

export default FlightsResultsPage;
