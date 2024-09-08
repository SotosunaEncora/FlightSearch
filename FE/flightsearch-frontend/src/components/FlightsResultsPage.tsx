import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Container, Box, Typography, Modal, Button } from '@mui/material';
import FlightCard from './FlightCard';

const FlightsResultsPage: React.FC = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { flights, departureAirport, arrivalAirport, departureDate, returnDate, adults, currency, nonStop } = location.state;
    const [noFlightsModalOpen, setNoFlightsModalOpen] = useState<boolean>(false);

    useEffect(() => {
        if (flights.length === 0) {
            setNoFlightsModalOpen(true);
        }
    }, [flights]);

    const handleCloseNoFlightsModal = () => {
        setNoFlightsModalOpen(false);
        navigate('/');
    };

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

            {/* No Flights Modal */}
            <Modal
                open={noFlightsModalOpen}
                onClose={handleCloseNoFlightsModal}
                aria-labelledby="no-flights-modal-title"
                aria-describedby="no-flights-modal-description"
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    border: '2px solid #000',
                    boxShadow: 24,
                    p: 4,
                }}>
                    <Typography id="no-flights-modal-title" variant="h6" component="h2">
                        No Flights Found
                    </Typography>
                    <Typography id="no-flights-modal-description" sx={{ mt: 2 }}>
                        Sorry, we couldn't find any flights matching your search criteria. Please try again with different parameters.
                    </Typography>
                    <Button onClick={handleCloseNoFlightsModal} sx={{ mt: 2 }}>
                        Back to Search
                    </Button>
                </Box>
            </Modal>
        </Container>
    );
};

export default FlightsResultsPage;
