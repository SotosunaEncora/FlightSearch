import React from 'react';
import { render, screen } from '@testing-library/react';
import FilterBox from './FilterBox';

describe('FilterBox', () => {
  test('renders autocomplete filter', () => {
    const onChange = jest.fn();
    render(
      <FilterBox
        label="Test Autocomplete"
        type="autocomplete"
        value=""
        options={[]}
        onChange={onChange}
      />
    );
    expect(screen.getByLabelText('Test Autocomplete')).toBeInTheDocument();
  });

  test('renders select filter', () => {
    const onChange = jest.fn();
    render(
      <FilterBox
        label="Test Select"
        type="select"
        value="Option 1"
        options={['Option 1', 'Option 2', 'Option 3']}
        onChange={onChange}
      />
    );
    expect(screen.getByRole('combobox', { name: 'Test Select' })).toBeInTheDocument();
  });
});