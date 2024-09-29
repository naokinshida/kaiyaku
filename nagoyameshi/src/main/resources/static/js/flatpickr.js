let maxDate = new Date();
maxDate.setMonth(maxDate.getMonth() + 3);

flatpickr('#reservationDay', {
    locale: 'ja',
    minDate: 'today',
    maxDate: maxDate
});