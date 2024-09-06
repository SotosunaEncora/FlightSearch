import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';
import FlightDetailsModal from './FlightDetailsModal';
import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';

dayjs.extend(duration);

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

    const formatDateTime = (dateTime: string) => dayjs(dateTime).format('DD-MM-YYYY @ HH:mm');
    const formatDurationTime = (durationStr: string) => {
        const dur = dayjs.duration(durationStr);
        return `${dur.hours()}:${dur.minutes()}`;
    };

    return (
        <>
            <Card variant="outlined" sx={{ width: '95%', margin: '1rem auto', cursor: 'pointer' }} onClick={handleOpen}>
                <CardContent sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Box sx={{ flex: 3 }}>
                        <Typography variant="h6">
                            {departureCity} ({flight.origin}) - {arrivalCity} ({flight.destination})
                        </Typography>
                        <Typography variant="body1">
                            {formatDateTime(flight.awayDepartureTime)} - {formatDateTime(flight.awayArrivalTime)}
                        </Typography>
                        <Typography variant="body1">
                            Airline: {flight.airline}
                        </Typography>
                    </Box>
                    <Box sx={{ flex: 1, textAlign: 'center' }}>
                        <Typography variant="body1">
                            Duration: {formatDurationTime(flight.awayDuration)}
                        </Typography>
                        <Typography variant="body1">
                            {flight.departureSegments.length > 1 ? `(${flight.departureSegments.length-1} stops)` : ''}
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
                                {formatDateTime(flight.returnSegments[0].departureTime)} - {formatDateTime(flight.returnSegments[flight.returnSegments.length - 1].arrivalTime)}
                            </Typography>
                            <Typography variant="body1">
                                Airline: {flight.returnSegments[0].airline}
                            </Typography>
                        </Box>
                        <Box sx={{ flex: 1, textAlign: 'center' }}>
                        <Typography variant="body1">
                            Duration: {formatDurationTime(flight.returnDuration)}
                        </Typography>
                        <Typography variant="body1">
                            {flight.returnSegments.length > 1 ? `(${flight.returnSegments.length-1} stops)` : ''}
                        </Typography>
                        </Box>
                        <Box sx={{ flex: 1, textAlign: 'right' }}>
                            {/* Additional content if needed */}
                        </Box>
                    </CardContent>
                )}
            </Card>
            <FlightDetailsModal open={open} onClose={handleClose} flight={flight} />
        </>
    );
};

export default FlightCard;

