# conact API Spec

## Create Contact

Enpoint : POST /api/contact

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
````json
{
  "firstName" : "Indra Dwi",
  "lastName" : "Prabowo",
  "email" : "indra@example.com",
  "phone" : "081339247514"
}
````

Response Body (Success) :
````json
{
  "data" : {
    "id" : "random-string",
    "firstName" : "Indra Dwi",
    "lastName" : "Prabowo",
    "email" : "indra@example.com",
    "phone" : "081339247514"
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "Email format invalid, phone format invalid, ..."
}
````

## Update Contact

Enpoint : PUT /api/contacts/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
````json
{
  "firstName" : "Indra Dwi",
  "lastName" : "Prabowo",
  "email" : "indra@example.com",
  "phone" : "081339247514"
}
````

Response Body (Success) :
````json
{
  "data" : {
    "id" : "random-string",
    "firstName" : "Indra Dwi",
    "lastName" : "Prabowo",
    "email" : "indra@example.com",
    "phone" : "081339247514"
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "Email format invalid, phone format invalid, ..."
}
````

## Get Contact

Enpoint : GET /api/contact/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : {
    "id" : "random-string",
    "firstName" : "Indra Dwi",
    "lastName" : "Prabowo",
    "email" : "indra@example.com",
    "phone" : "081339247514"
  }
}
````

Response Body (Failed, 404) :
````json
{
  "error" : "Contact is not found"
}
````

## Search Contact

Enpoint : GET /api/contact

Query Param : 
- name : String, contact firstName or lastName, using like query, optional
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : [
    {
      "id" : "random-string",
      "firstName" : "Indra Dwi",
      "lastName" : "Prabowo",
      "email" : "indra@example.com",
      "phone" : "081339247514"
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "unauthorizer"
}
````

## Remove Contact

Enpoint : DELETE api/contact/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : "OK"
}
````

Response Body (Failed) :
````json
{
  "error" : "Contact is not found"
}
````