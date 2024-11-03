export const toLocalDate = (date) => {
    const temp = new Date(date)
    temp.setMinutes(temp.getMinutes() - temp.getTimezoneOffset());
    return temp.toISOString().slice(0, 16)
}

export const fromLocalDate = (localDate) => {
    const temp = new Date(localDate)
    return temp.toISOString()
}
