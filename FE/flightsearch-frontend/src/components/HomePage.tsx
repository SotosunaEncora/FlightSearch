import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Button, Modal, Typography, Checkbox, FormControlLabel } from '@mui/material';
import axios from 'axios';
import FilterBox from './FilterBox';

interface AirportOption {
    code: string;
    cityName: string;
    name: string;
}

const HomePage: React.FC = () => {
    const navigate = useNavigate();
    const [departureAirport, setDepartureAirport] = useState<AirportOption | null>(null);
    const [arrivalAirport, setArrivalAirport] = useState<AirportOption | null>(null);
    const [departureDate, setDepartureDate] = useState<string | null>(null);
    const [returnDate, setReturnDate] = useState<string | null>(null);
    const [adults, setAdults] = useState<number>(1);
    const [currency, setCurrency] = useState<string>('USD');
    const [nonStop, setNonStop] = useState<boolean>(false);
    const [departureOptions, setDepartureOptions] = useState<AirportOption[]>([]);
    const [arrivalOptions, setArrivalOptions] = useState<AirportOption[]>([]);
    const [errorModalOpen, setErrorModalOpen] = useState<boolean>(false);
    const [departureKeyword, setDepartureKeyword] = useState<string>('a');
    const [arrivalKeyword, setArrivalKeyword] = useState<string>('a');

    // Backup airport options
    const backupAirportOptions: AirportOption[] = [
        { code: 'JFK', cityName: 'New York', name: 'John F. Kennedy International Airport' },
        { code: 'LAX', cityName: 'Los Angeles', name: 'Los Angeles International Airport' },
        { code: 'ORD', cityName: 'Chicago', name: 'O\'Hare International Airport' },
        { code: 'LHR', cityName: 'London', name: 'Heathrow Airport' },
        { code: 'CDG', cityName: 'Paris', name: 'Charles de Gaulle Airport' },
        { code: 'NRT', cityName: 'Tokyo', name: 'Narita International Airport' },
        { code: 'SYD', cityName: 'Sydney', name: 'Sydney Airport' },
    ];

    useEffect(() => {
        // Fetch departure airport options from the back-end
        const fetchDepartureOptions = async (keyword: string) => {
            try {
                const response = await axios.get<AirportOption[]>(`http://localhost:8080/airports/${keyword}`);
                setDepartureOptions(response.data);
            } catch (error) {
                console.error('Error fetching departure airport options:', error);
                setDepartureOptions(backupAirportOptions);
                setErrorModalOpen(true);
            }
        };

        if (departureKeyword) {
            fetchDepartureOptions(departureKeyword);
        }
    }, [departureKeyword]);

    useEffect(() => {
        // Fetch arrival airport options from the back-end
        const fetchArrivalOptions = async (keyword: string) => {
            try {
                const response = await axios.get<AirportOption[]>(`http://localhost:8080/airports/${keyword}`);
                setArrivalOptions(response.data);
            } catch (error) {
                console.error('Error fetching arrival airport options:', error);
                setArrivalOptions(backupAirportOptions);
                setErrorModalOpen(true);
            }
        };

        if (arrivalKeyword) {
            fetchArrivalOptions(arrivalKeyword);
        }
    }, [arrivalKeyword]);

    const handleSearch = async () => {
        // Prepare search parameters
        const searchParams = {
            departureAirport: departureAirport?.code,
            arrivalAirport: arrivalAirport?.code,
            departureDate,
            returnDate,
            adults,
            currency,
            nonStop,
        };
        console.log('searchParams: ', searchParams);
        try {
            const response = await axios.post('http://localhost:8080/flights/search', searchParams);
            console.log('Flight search results:', response.data);
            navigate('/results', {
                state: {
                    flights: response.data,
                    departureAirport,
                    arrivalAirport,
                    departureDate,
                    returnDate,
                    adults,
                    currency,
                    nonStop,
                }
            });
        } catch (error) {
            console.error('Error searching flights:', error);
            setErrorModalOpen(true);
            navigate('/results', {
                state: {
                    flights: [],
                    departureAirport,
                    arrivalAirport,
                    departureDate,
                    returnDate,
                    adults,
                    currency,
                    nonStop,
                }
            });
        }
    };

    const handleCloseErrorModal = () => {
        setErrorModalOpen(false);
    };

    return (
        <>
            <Container
                sx={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    minHeight: 'calc(100vh - 64px)', // Adjust for NavBar height
                    paddingTop: '2rem',
                    paddingBottom: '2rem',
                }}
            >
                <Box
                    sx={{
                        width: '100%',
                        maxWidth: 600,
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '1rem',
                    }}
                >
                    <FilterBox
                        label="Departure Airport"
                        type="autocomplete"
                        value={departureAirport ? { ...departureAirport } : null}
                        options={departureOptions}
                        onChange={(event, newValue) => setDepartureAirport(newValue)}
                        onInputChange={(event, newInputValue) => setDepartureKeyword(newInputValue)}
                        getOptionLabel={(option: AirportOption) => `${option.code} - ${option.cityName}`}
                    />
                    <FilterBox
                        label="Arrival Airport"
                        type="autocomplete"
                        value={arrivalAirport ? { ...arrivalAirport } : null}
                        options={arrivalOptions}
                        onChange={(event, newValue) => setArrivalAirport(newValue)}
                        onInputChange={(event, newInputValue) => setArrivalKeyword(newInputValue)}
                        getOptionLabel={(option: AirportOption) => `${option.code} - ${option.cityName}`}
                    />
                    <FilterBox
                        label="Departure Date"
                        type="date"
                        value={departureDate}
                        onChange={(event, newValue) => setDepartureDate(newValue)}
                        fullWidth
                    />
                    <FilterBox
                        label="Return Date"
                        type="date"
                        value={returnDate}
                        onChange={(event, newValue) => setReturnDate(newValue)}
                        fullWidth
                    />
                    <FilterBox
                        label="Number of Adults"
                        type="number"
                        value={adults}
                        onChange={(event, newValue) => setAdults(newValue)}
                    />
                    <FilterBox
                        label="Currency"
                        type="select"
                        value={currency}
                        options={['USD', 'MXN', 'EUR']}
                        onChange={(event, newValue) => setCurrency(newValue)}
                    />
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={nonStop}
                                onChange={(e) => setNonStop(e.target.checked)}
                                color="primary"
                            />
                        }
                        label="Non-stop"
                    />
                    <Button variant="contained" color="primary" onClick={handleSearch} fullWidth>
                        Search Flights
                    </Button>
                </Box>
            </Container>
            <Modal
                open={errorModalOpen}
                onClose={handleCloseErrorModal}
                aria-labelledby="error-modal-title"
                aria-describedby="error-modal-description"
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
                    <Typography id="error-modal-title" variant="h6" component="h2">
                        Error
                    </Typography>
                    <Typography id="error-modal-description" sx={{ mt: 2 }}>
                        An error occurred while fetching data. Please try again later.
                    </Typography>
                    <Button onClick={handleCloseErrorModal} sx={{ mt: 2 }}>
                        Close
                    </Button>
                </Box>
            </Modal>
        </>
    );
};

export default HomePage;