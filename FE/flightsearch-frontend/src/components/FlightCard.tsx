import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';
import FlightDetailsModal from './FlightDetailsModal';

interface FlightCardProps {
    flight: any; // Adjust the type according to your data structure
    departureCity: string;
    departureAirport: string;
    arrivalCity: string;
    arrivalAirport: string;
    currency: string;
    adults: number;
}

const FlightCard: React.FC<FlightCardProps> = ({ flight, departureCity, departureAirport, arrivalCity, arrivalAirport, currency, adults }) => {
    const [open, setOpen] = React.useState(false);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const hasReturnSegments = flight.returnSegments && flight.returnSegments.length > 0;

    return (
        <>
            <Card variant="outlined" sx={{ width: hasReturnSegments ? '180%' : '90%', margin: '1rem auto', cursor: 'pointer' }} onClick={handleOpen}>
                <CardContent sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Box sx={{ flex: 3 }}>
                        <Typography variant="h6">
                            {departureCity} ({flight.origin}) - {arrivalCity} ({flight.destination})
                        </Typography>
                        <Typography variant="body1">
                            {flight.awayDepartureTime} - {flight.awayArrivalTime}
                        </Typography>
                        <Typography variant="body1">
                            Airline: {flight.airline}
                        </Typography>
                    </Box>
                    <Box sx={{ flex: 1, textAlign: 'center' }}>
                        <Typography variant="body1">
                            Duration: {flight.awayDuration}
                        </Typography>
                    </Box>
                    <Box sx={{ flex: 1, textAlign: 'right' }}>
                        <Typography variant="h6">
                            {flight.priceBreakdown.total} {currency}
                        </Typography>
                        <Typography variant="body1">
                            Price per traveler
                        </Typography>
                        <Typography variant="body1">
                            {flight.priceBreakdown.pricePerTraveler} {currency}
                        </Typography>
                    </Box>
                </CardContent>
                {hasReturnSegments && (
                    <CardContent sx={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem' }}>
                        <Box sx={{ flex: 3 }}>
                            <Typography variant="h6">
                                {arrivalCity} ({flight.destination}) - {departureCity} ({flight.origin})
                            </Typography>
                            <Typography variant="body1">
                                {flight.returnSegments[0].departureTime} - {flight.returnSegments[flight.returnSegments.length - 1].arrivalTime}
                            </Typography>
                            <Typography variant="body1">
                                Duration: {flight.returnSegments.reduce((total: number, segment: { duration: number }) => total + segment.duration, 0)}
                
                            </Typography>
                            <Typography variant="body1">
                                Airline: {flight.returnSegments[0].airline}
                            </Typography>
                        </Box>
                        <Box sx={{ flex: 1, textAlign: 'center' }}>
                            <Typography variant="body1">
                                Duration: {flight.returnSegments.reduce((total: number, segment: { duration: number }) => total + segment.duration, 0)}
                            </Typography>
                        </Box>
                        <Box sx={{ flex: 1, textAlign: 'right' }}>
                            <Typography variant="h6">
                                {flight.priceBreakdown.total} {currency}
                            </Typography>
                            <Typography variant="body1">
                                Price per traveler
                            </Typography>
                            <Typography variant="body1">
                                {flight.priceBreakdown.pricePerTraveler} {currency}
                            </Typography>
                        </Box>
                    </CardContent>
                )}
            </Card>
            <FlightDetailsModal open={open} onClose={handleClose} flight={flight} />
        </>
    );
};

export default FlightCard;

