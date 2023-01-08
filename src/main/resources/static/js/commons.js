function getHeaders() {
    const token = document.getElementsByName("_csrf")[0].getAttribute('content')
    const headerName = document.getElementsByName("_csrf_header")[0].getAttribute('content')
    const headers = {
        'Content-Type': 'application/json'
    }
    headers[headerName] = token
    return headers
}