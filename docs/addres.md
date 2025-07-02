# Addres API Spec

## Create Addres

Enpoint : POST /api/contact/{idContact}/addres

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
````json
{
  "street" : "Jalan apa",
  "city" : "Kota",
  "porvince" : "Provinsi",
  "country" : "Negara",
  "postletCode" : 12345
}
````

Response Body (Success) :
````json
{
  "data" : {
    "id" : "randomstring",
    "street" : "Jalan apa",
    "city" : "Kota",
    "porvince" : "Provinsi",
    "country" : "Negara",
    "postletCode" : 12345
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "Contact is not found"
}
````


## Update Addres

Enpoint : PUT /api/contact/{idContact}/addresses/{idAddress}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
````json
{
  "street" : "Jalan apa",
  "city" : "Kota",
  "porvince" : "Provinsi",
  "country" : "Negara",
  "postletCode" : 12345
}
````

Response Body (Success) :
````json
{
  "data" : {
    "id" : "randomstring",
    "street" : "Jalan apa",
    "city" : "Kota",
    "porvince" : "Provinsi",
    "country" : "Negara",
    "postletCode" : 12345
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "Address is not found"
}
````
## Get Addres

Enpoint : GET /api/contact/{idContact}/addresses/{idAddress}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : {
    "id" : "randomstring",
    "street" : "Jalan apa",
    "city" : "Kota",
    "porvince" : "Provinsi",
    "country" : "Negara",
    "postletCode" : 12345
  }
}
````

Response Body (Failed) :
````json
{
  "error" : "Address is not found"
}
````

## Remove Addres

Enpoint : DELETE /api/contact/{idContact}/addresses/{idAddress}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : "Ok"
}
````

Response Body (Failed) :
````json
{
  "error" : "Address is not found"
}
````

## List Addres

Enpoint : GET /api/contact/{idContact}/addresses

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
````json
{
  "data" : [
    {
      "id" : "randomstring",
      "street" : "Jalan apa",
      "city" : "Kota",
      "porvince" : "Provinsi",
      "country" : "Negara",
      "postletCode" : 12345
    }
  ]
}
````

Response Body (Failed) :
````json
{
  "error" : "Contact is not found"
}
````