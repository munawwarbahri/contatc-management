# Contact API Spec

## Create Contact

Endpoint: GET /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data": {
    "id": "random-string",
    "firstName": "Bahri",
    "lastName": "Munawwar",
    "email": "bahri@example.com",
    "phone": "081234567890"
  }
}
```

Response Body (Failed):

```json
{
  "error": "Email format invalid, phone format invalid, ..."
}
```

## Update Contact

Endpoint: PUT /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (Mandatory)

Request Body:

```json
{
   "firstName": "Bahri",
    "lastName": "Munawwar",
    "email": "bahri@example.com",
    "phone": "081234567890"
}
```

Response Body (Success):

```json
{
  "data": {
    "id": "random string",
    "firstName": "Bahri",
    "lastName": "Munawwar",
    "email": "bahri@example.com",
    "phone": "081234567890"
  }
}
```

Response Body (Failed):

```json
{
  "error": "Email format invalid, phone format invalid, ..."
}
```

## Get Contact

Endpoint: GET /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data": {
    "id": "random string",
    "firstName": "Bahri",
    "lastName": "Munawwar",
    "email": "bahri@example.com",
    "phone": "081234567890"
  }
}
```

Response Body (Failed, 404):

```json
{
  "error": "Contact are not found"
}
```

## Search Contact

Endpoint: GET /api/contacts/search

Query Param:

- name : String, contact first name or last name, using like query, optional "
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header:

- X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data": [
    {
      "id": "random string",
      "firstName": "bahri",
      "lastName": "munawwar",
      "email": "bahri@example.com"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body (Failed):

```json
{
  "error": "Unauthorized"
}
```

## Remove Contact

Endpoint: DELETE /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (Mandatory)

Response Body (Success):

````json
{
  "data": "Contact has been removed"
}
````

Response Body (Failed):

```json
{
  "error": "Contact are not found"
}
```