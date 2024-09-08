import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Button, Modal, Typography, Checkbox, FormControlLabel, Alert, CircularProgress } from '@mui/material';
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
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [validationModalOpen, setValidationModalOpen] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [dateValidationModalOpen, setDateValidationModalOpen] = useState<boolean>(false);
    const [dateValidationMessage, setDateValidationMessage] = useState<string>('');

    useEffect(() => {
        const fetchDepartureOptions = async (keyword: string) => {
            try {
                const response = await axios.get<AirportOption[]>(`http://localhost:8080/airports/${keyword}`);
                setDepartureOptions(response.data);
            } catch (error) {
                console.error('Error fetching departure airport options:', error);
                setErrorMessage('Error fetching departure airport options.');
                setErrorModalOpen(true);
            }
        };

        if (departureKeyword) {
            fetchDepartureOptions(departureKeyword);
        }
    }, [departureKeyword]);

    useEffect(() => {
        const fetchArrivalOptions = async (keyword: string) => {
            try {
                const response = await axios.get<AirportOption[]>(`http://localhost:8080/airports/${keyword}`);
                setArrivalOptions(response.data);
            } catch (error) {
                console.error('Error fetching arrival airport options:', error);
                setErrorMessage('Error fetching arrival airport options.');
                setErrorModalOpen(true);
            }
        };

        if (arrivalKeyword) {
            fetchArrivalOptions(arrivalKeyword);
        }
    }, [arrivalKeyword]);

    const validateDates = (newDepartureDate: string | null, newReturnDate: string | null) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Set to beginning of the day for accurate comparison

        if (newDepartureDate) {
            const departureDateTime = new Date(newDepartureDate);
            departureDateTime.setHours(0, 0, 0, 0); // Normalize to start of day
            if (departureDateTime < today) {
                setDateValidationMessage("Departure date cannot be earlier than today.");
                setDateValidationModalOpen(true);
                setDepartureDate(null);
                return false;
            }
        }

        if (newReturnDate) {
            const returnDateTime = new Date(newReturnDate);
            returnDateTime.setHours(0, 0, 0, 0); // Normalize to start of day
            if (returnDateTime < today) {
                setDateValidationMessage("Return date cannot be earlier than today.");
                setDateValidationModalOpen(true);
                setReturnDate(null);
                return false;
            }
        }

        if (newDepartureDate && newReturnDate) {
            const departureDateTime = new Date(newDepartureDate);
            const returnDateTime = new Date(newReturnDate);
            departureDateTime.setHours(0, 0, 0, 0); // Normalize to start of day
            returnDateTime.setHours(0, 0, 0, 0); // Normalize to start of day
            
            if (returnDateTime < departureDateTime) {
                setDateValidationMessage("Return date cannot be earlier than departure date.");
                setDateValidationModalOpen(true);
                setReturnDate(null);
                return false;
            }
        }

        return true;
    };

    const handleDepartureDateChange = (event: React.ChangeEvent<{}>, newValue: string | null) => {
        if (validateDates(newValue, returnDate)) {
            setDepartureDate(newValue);
        }
    };

    const handleReturnDateChange = (event: React.ChangeEvent<{}>, newValue: string | null) => {
        if (validateDates(departureDate, newValue)) {
            setReturnDate(newValue);
        }
    };

    const handleCloseDateValidationModal = () => {
        setDateValidationModalOpen(false);
    };

    const handleSearch = async () => {
        // Validate essential inputs
        if (!departureAirport || !arrivalAirport || !departureDate) {
            setErrorMessage('Please fill in all required fields: Departure Airport, Arrival Airport, and Departure Date.');
            setValidationModalOpen(true);
            return;
        }

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
        setIsLoading(true);
        try {
            const response = await axios.post('http://localhost:8080/flights/search', searchParams);
            console.log('Flight search results:', response.data);
            setIsLoading(false);
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
            setIsLoading(false);
            if (axios.isAxiosError(error)) {
                setErrorMessage(error.response?.data?.message || 'An error occurred while searching for flights.');
            } else {
                setErrorMessage('An unexpected error occurred while searching for flights.');
            }
            setErrorModalOpen(true);
        }
    };

    const handleCloseErrorModal = () => {
        setErrorModalOpen(false);
    };

    const handleCloseValidationModal = () => {
        setValidationModalOpen(false);
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
                        onChange={handleDepartureDateChange}
                        fullWidth
                    />
                    <FilterBox
                        label="Return Date"
                        type="date"
                        value={returnDate}
                        onChange={handleReturnDateChange}
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
                    <Alert severity="error" sx={{ mt: 2 }}>
                        {errorMessage}
                    </Alert>
                    <Button onClick={handleCloseErrorModal} sx={{ mt: 2 }}>
                        Close
                    </Button>
                </Box>
            </Modal>

            <Modal
                open={validationModalOpen}
                onClose={handleCloseValidationModal}
                aria-labelledby="validation-modal-title"
                aria-describedby="validation-modal-description"
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
                    <Typography id="validation-modal-title" variant="h6" component="h2">
                        Missing Information
                    </Typography>
                    <Alert severity="warning" sx={{ mt: 2 }}>
                        {errorMessage}
                    </Alert>
                    <Button onClick={handleCloseValidationModal} sx={{ mt: 2 }}>
                        Close
                    </Button>
                </Box>
            </Modal>

            {/* Loading Modal */}
            <Modal
                open={isLoading}
                aria-labelledby="loading-modal-title"
                aria-describedby="loading-modal-description"
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    borderRadius: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}>
                    <CircularProgress />
                    <Typography sx={{ mt: 2 }}>Searching for flights...</Typography>
                </Box>
            </Modal>

            {/* Date Validation Modal */}
            <Modal
                open={dateValidationModalOpen}
                onClose={handleCloseDateValidationModal}
                aria-labelledby="date-validation-modal-title"
                aria-describedby="date-validation-modal-description"
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
                    <Typography id="date-validation-modal-title" variant="h6" component="h2">
                        Invalid Date Selection
                    </Typography>
                    <Alert severity="warning" sx={{ mt: 2 }}>
                        {dateValidationMessage}
                    </Alert>
                    <Button onClick={handleCloseDateValidationModal} sx={{ mt: 2 }}>
                        Close
                    </Button>
                </Box>
            </Modal>
        </>
    );
};

export default HomePage;