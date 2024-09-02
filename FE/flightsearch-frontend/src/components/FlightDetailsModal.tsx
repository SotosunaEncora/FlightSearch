import React from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography, Box, Card, CardContent } from '@mui/material';

interface FlightDetailsModalProps {
    open: boolean;
    onClose: () => void;
    flight: any; // Adjust the type according to your data structure
}

const FlightDetailsModal: React.FC<FlightDetailsModalProps> = ({ open, onClose, flight }) => {
    return (
        <Dialog open={open} onClose={onClose} maxWidth="lg" fullWidth>
            <DialogTitle>Flight Details</DialogTitle>
            <DialogContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Box sx={{ flex: 4, marginRight: '1rem' }}>
                        <Typography variant="h6">Departure Segments:</Typography>
                        {flight.departureSegments && flight.departureSegments.length > 0 ? (
                            flight.departureSegments.map((segment: any, index: number) => (
                                <Card key={index} variant="outlined" sx={{ marginBottom: '1rem', display: 'flex', justifyContent: 'space-between' }}>
                                    <CardContent sx={{ flex: 3 }}>
                                        <Typography variant="body2">
                                            Segment {index + 1}:
                                        </Typography>
                                        <Typography variant="body2">
                                            {segment.origin} to {segment.destination}
                                        </Typography>
                                        <Typography variant="body2">
                                            Departure: {segment.departureTime}
                                        </Typography>
                                        <Typography variant="body2">
                                            Arrival: {segment.arrivalTime}
                                        </Typography>
                                        <Typography variant="body2">
                                            Airline: {segment.airline}
                                        </Typography>
                                        <Typography variant="body2">
                                            Flight Number: {segment.flightNumber}
                                        </Typography>
                                        <Typography variant="body2">
                                            Aircraft: {segment.aircraft}
                                        </Typography>
                                        <Typography variant="body2">
                                            Duration: {segment.duration}
                                        </Typography>
                                    </CardContent>
                                    {segment.details && (
                                        <Box sx={{ flex: 1, border: '1px solid', padding: '0.5rem', marginLeft: '1rem' }}>
                                            <Typography variant="body2">
                                                Cabin: {segment.details.fareCabin}
                                            </Typography>
                                            <Typography variant="body2">
                                                Class: {segment.details.fareClass}
                                            </Typography>
                                            <Typography variant="body2">
                                                Fare Basis: {segment.details.fareBasis}
                                            </Typography>
                                            <Typography variant="body2">
                                                Checked Bags: {segment.details.checkedBags.join(', ')}
                                            </Typography>
                                        </Box>
                                    )}
                                </Card>
                            ))
                        ) : (
                            <Typography variant="body2">No segment details available.</Typography>
                        )}
                        {flight.returnSegments && flight.returnSegments.length > 0 && (
                            <>
                                <Typography variant="h6">Return Segments:</Typography>
                                {flight.returnSegments.map((segment: any, index: number) => (
                                    <Card key={index} variant="outlined" sx={{ marginBottom: '1rem', display: 'flex', justifyContent: 'space-between' }}>
                                        <CardContent sx={{ flex: 3 }}>
                                            <Typography variant="body2">
                                                Segment {index + 1}:
                                            </Typography>
                                            <Typography variant="body2">
                                                {segment.origin} to {segment.destination}
                                            </Typography>
                                            <Typography variant="body2">
                                                Departure: {segment.departureTime}
                                            </Typography>
                                            <Typography variant="body2">
                                                Arrival: {segment.arrivalTime}
                                            </Typography>
                                            <Typography variant="body2">
                                                Airline: {segment.airline}
                                            </Typography>
                                            <Typography variant="body2">
                                                Flight Number: {segment.flightNumber}
                                            </Typography>
                                            <Typography variant="body2">
                                                Aircraft: {segment.aircraft}
                                            </Typography>
                                            <Typography variant="body2">
                                                Duration: {segment.duration}
                                            </Typography>
                                        </CardContent>
                                        {segment.details && (
                                            <Box sx={{ flex: 1, border: '1px solid', padding: '0.5rem', marginLeft: '1rem' }}>
                                                <Typography variant="body2">
                                                    Cabin: {segment.details.fareCabin}
                                                </Typography>
                                                <Typography variant="body2">
                                                    Class: {segment.details.fareClass}
                                                </Typography>
                                                <Typography variant="body2">
                                                    Fare Basis: {segment.details.fareBasis}
                                                </Typography>
                                                <Typography variant="body2">
                                                    Checked Bags: {segment.details.checkedBags.join(', ')}
                                                </Typography>
                                            </Box>
                                        )}
                                    </Card>
                                ))}
                            </>
                        )}
                    </Box>
                    <Box sx={{ flex: 1 }}>
                        <Typography variant="h6">Price Breakdown:</Typography>
                        {flight.priceBreakdown ? (
                            <>
                                <Typography variant="body2">
                                    Base Price: {flight.priceBreakdown.base} {flight.priceBreakdown.currency}
                                </Typography>
                                <Typography variant="body2">
                                    Total Price: {flight.priceBreakdown.total} {flight.priceBreakdown.currency}
                                </Typography>
                                <Typography variant="body2">
                                    Fees: {flight.priceBreakdown.fees} {flight.priceBreakdown.currency}
                                </Typography>
                                <Typography variant="body2">
                                    Price per Traveler: {flight.priceBreakdown.pricePerTraveler} {flight.priceBreakdown.currency}
                                </Typography>
                            </>
                        ) : (
                            <Typography variant="body2">No price breakdown available.</Typography>
                        )}
                    </Box>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="primary">
                    Close
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default FlightDetailsModal;
