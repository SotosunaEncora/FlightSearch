import React from 'react';
import { TextField, Autocomplete, Checkbox, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';

interface AirportOption {
    iataCode: string;
    cityName: string;
    name: string;
}

interface FilterBoxProps {
    label: string;
    type: 'autocomplete' | 'date' | 'number' | 'select';
    value: any;
    options?: any[];
    onChange: (event: any, newValue: any) => void;
    onInputChange?: (event: any, newInputValue: any) => void; // Add this line
    getOptionLabel?: (option: any) => string;
    fullWidth?: boolean;
}

const FilterBox: React.FC<FilterBoxProps> = ({
    label,
    type,
    value,
    options = [],
    onChange,
    onInputChange, // Add this line
    getOptionLabel,
    fullWidth
}) => {
    switch (type) {
        case 'autocomplete':
            return (
                <Autocomplete
                    value={value}
                    options={options}
                    onChange={onChange}
                    onInputChange={onInputChange} // Add this line
                    getOptionLabel={getOptionLabel}
                    renderInput={(params) => <TextField {...params} label={label} variant="outlined" fullWidth={true} />}
                />
            );
        case 'date':
            return (
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                        label={label}
                        value={value ? dayjs(value) : null}
                        onChange={(newValue) => onChange(null, newValue ? dayjs(newValue).format('YYYY-MM-DD') : '')}
                        format="YYYY-MM-DD"
                        slotProps={{ textField: { fullWidth }}}
                    />
                </LocalizationProvider>
            );
        case 'number':
            return (
                <TextField
                    label={label}
                    type="number"
                    value={value}
                    onChange={(e) => onChange(e, e.target.value ? Number(e.target.value) : '')}
                    fullWidth={fullWidth}
                />
            );
        case 'select':
            return (
                <FormControl fullWidth={fullWidth}>
                    <InputLabel id={`${label}-label`}>{label}</InputLabel>
                    <Select
                        labelId={`${label}-label`}
                        id={label}
                        value={value}
                        label={label}
                        onChange={onChange}
                    >
                        {options.map((option) => (
                            <MenuItem key={option} value={option}>
                                {option}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            );
        default:
            return null;
    }
};

export default FilterBox;