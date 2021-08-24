# COVID API

Esta aplicación va a permitir al usuario realizar distintas consultas sobre sus países de preferencia orientadas a la información públicamente conocida sobre el COVID-19.

## Tecnologías

Las tecnologías utilizadas en este proyecto son:

- Spring Boot como framework base
- Java 14 OpenJDK14.
- Gradle: gestor de paquetes.
- Lombok Annotations: Para generacion de Getters/Setters/Constructors simplifica el armado de DTOs & Entities https://projectlombok.org/ es recomendable instalar el plugin correspondiente para cada IDE (para visualizar correctamente)
- JUnit & Mockito: herramientas para test 
- Docker: gestor de containers para correr la aplicación y poder desacoplarnos del Sistema Operativo donde corren los distintos componentes de la aplicación.
- Docker Compose: Herramienta para poder correr aplicaciones compuestas por varios contenedores.
- DB H2: persistencia in memory cuando la aplicación corre en modo local.
- JWT Tokens: tokens autocontenidos para realizar la autenticación de los usuarios.
- Java Web Security: herramienta para administrar la autorización de los endpoints.
- MySQL: base de datos que se utiliza cuando se ejecuta en modo contenedor local.
- SQL Server: base de datos que se utiliza cuando se ejecuta en containers en la nube.

## Endpoints de la API

Esta sección va a estar dividida por entidad y su fin es brindar documentación extensiva sobre cada endpoint de la API (puede ser reemplazada por una herramienta del tipo Swagger en algún futuro).

### Login/Creación de usuario
- **POST /sign-up**: Para dar de alta un usuario en el sistema.

    #####  Enviar:
    ```json
    {
        "user_name": "UserPruebaSignUp",
        "password": "123456",
        "name": "Pepe",
        "last_name": "Flores",
        "email": "pepeflores@gmail.com"
    }
    ```
    #####  Devuelve:
    ```json
    {
        "id": 3,
        "user_name": "UserPruebaSignUp",
        "password": "*Protected*",
        "name": "Pepe",
        "last_name": "Flores",
        "email": "pepeflores@gmail.com"
    }
    ```
- **POST /login**: Para autenticar un usuario en el sistema.

    #####  Enviar:
    ```json
    {
        "user_name": "UserPruebaSignUp",
        "password": "123456"
    }
    ```

    #####  Devuelve:
    ```json
    {
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVc2VyUHJ1ZWJhU2lnblVwIiwiaWF0IjoxNTkxOTA3MDUzLCJpc3MiOiJncnVwbzEudGFjcy11dG4uY29tLmFyIiwiZXhwIjoxNTkxOTM1ODUzfQ.w5IrPBty7xYoIHmgqrIJ6kGJmdiynZWD4_cRLx7XbvOo4o9eOsS0vWKz9yK2MlXOg0mNHp2JhOddVJAxcHjmPQ"
    }
    ```
- ~~**POST /logout**: Procesa el Logout del usuario.~~

    Al utilizar tokens JWT con una validez predeterminada (8 horas), el logout se procesa en el frontend, eliminando el Token del lado del usuario.

### Método de autenticación

- Una vez que obtenemos el token, a cada request que hagamos contra la API (GET/POST), le tenemos que agregar el header "Authorization", con el valor:

    ```
    Authorization: "Bearer token-generated-after-login"
    ``` 

### Países
- **GET /countries**: Obtener el listado de países.

- **GET /countries/{country-id}**: Obtener la información de un país en particular.

    #####  Devuelve:
    ```json
    {
        "id": 1,
        "name": "Argentina",
        "iso_country_code": "AR",
        "start_date": "2020-03-03T00:00:00-03:00",
        "offset": 41,
        "strategy": "Cuarentena"
    }
    ```
  
    Strategy puede ser uno de ("Distanciamiento Social", "Cuarentena", "Libre Circulación", "No Analizado").
  
- **PUT /countries/{country-id}**: Actualizar la estrategia de un país (como administrador).

    #####  Enviar:
    ```json
    {
        "id": 1,
        "name": "Argentina", 
        "iso_country_code": "AR", 
        "start_date": "2020-03-03T00:00:00-03:00", 
        "offset": 41, 
        "strategy": "Distanciamiento Social"
    }
    ```
  
    Los campos Name, Iso Country Code, Start Date y Offset, son ignorados al actualizar la estrategia de un país.
  
    #####  Devuelve:
    ```json
    {
        "id": 1,
        "name": "Argentina",
        "iso_country_code": "AR",
        "start_date": "2020-03-03T00:00:00-03:00",
        "offset": 41,
        "strategy": "Distanciamiento Social"
    }
    ```
  
- **GET /countries-page**: Obtener la lista de países de manera paginada

    - **Query Params**:
        - **prefix**: Prefijo de búsqueda del País (A, Ar, Arg ...)
        - **pageNum**: Número de página (0, 1, 2, ...)
  
    #####  Enviamos:
    ```
    /countries-page?prefix=Ar&pageNum=0
    ```
  
    #####  Devuelve:
    ```json
    {
        "page_number": 0,
        "total_pages": 1,
        "total_elements": 4,
        "country_list": [
            {
                "id": 198,
                "name": "Arabia Saudí",
                "iso_country_code": "SA",
                "start_date": "2020-03-02T00:00:00-03:00",
                "offset": 40,
                "strategy": "No Analizado"
            },
            {
                "id": 4,
                "name": "Argelia",
                "iso_country_code": "DZ",
                "start_date": "2020-02-25T00:00:00-03:00",
                "offset": 34,
                "strategy": "No Analizado"
            },
            {
                "id": 11,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41,
                "strategy": "No Analizado"
            },
            {
                "id": 12,
                "name": "Armenia",
                "iso_country_code": "AM",
                "start_date": "2020-03-01T00:00:00-03:00",
                "offset": 39,
                "strategy": "No Analizado"
            }
        ]
    }
    ```
  
- **GET /country-interest/{country-id}**: Obtiene la cantidad de usuarios que se interesaron en el mismo (lo agregaron a una lista) (como administrador).

    #####  Devuelve:
    ```json
    {
        "country_name": "Argentina",
        "total_users": 2,
        "users_names": [
            "Administrator",
            "Usuario1"
        ]
    }
    ```

### Usuarios
- **GET /users/all**: Obtener la lista de usuarios (administrador).
- **GET /users**: Acceder a la información de mi usuario (usuario y administrador).

    #####  Devuelve:
    ```json
    {
        "username": "pepito",
        "last_name": "pepe",
        "email": "pepe@gmail.com",
        "lists_total": 3,
        "countries_total": 6,
        "last_accessed": "1/22/20 01:24:54"
    }
    ```

- **GET /users/country-lists**: Obtener las listas de mi usuario (usuario y administrador).

    #####  Devuelve:
    ```json
    [
        {
            "id": 4,
            "name": "Lista ejemplo",
            "countries": [
                {
                    "id": 11,
                    "name": "Argentina",
                    "iso_country_code": "AR",
                    "start_date": "2020-03-03T00:00:00-03:00",
                    "offset": 41,
                    "strategy": "No Analizado"
                },
                {
                    "id": 27,
                    "name": "Bolivia",
                    "iso_country_code": "BO",
                    "start_date": "2020-03-11T00:00:00-03:00",
                    "offset": 49,
                    "strategy": "No Analizado"
                }
            ],
            "user_id": 4
        }
    ]
    ```

- **GET /user-information/{user-id}**: Obtiene la información de un usuario en particular (administrador).

    #####  Devuelve:
    ```json
    {
        "id": 1,
        "user_name": "Administrator",
        "telegram_id": null,
        "name": "Juan",
        "last_name": "Perez",
        "email": "juanperez@aol.com",
        "total_lists": 4,
        "total_countries": 16,
        "last_login": "2020-06-28T03:00:33.741245"
    }
    ```
  
### Usuario Telegram
- **POST /user-telegram**: Asocia el id de telegram a un usuario que ya existe.
- Request:
```json
{
    "telegram_id":100000,
    "user_name":"userpp",
    "password":"12345678"
}
```
- **GET /user-telegram/{telegramId}**: Validar que el id de telegram esta asociado a un usuario.
- **DELETE /user-telegram/{telegramId}**: Desasocia el id de telegram del usuario.
- Response:
```json
{
    "userName": "userpp",
    "telegramId": 100,
    "name": "pepito",
    "lastName": "pepe",
    "email": "pepe@gmail.com",
    "totalLists": 0,
    "totalCountries": 0
}
```
### Listas
- **POST /country-lists**:  Crear una lista asociada a mi usuario (usuario y administrador).
- *NOTA: Un usuario no puede crear más de una lista con el mismo nombre, el nombre es case insensitive. Si intenta crearla el enpoint responde: Status 409 Conflict - Country List with same name already exists*


    #####  Enviar:
    ```json
    {
        "name": "Lista ejemplo",
        "countries": 
        [
            {
                "iso_country_code": "PY"
            },
            {
                "iso_country_code": "CL"
            }
        ]
    }
    ```
  
    #####  Devuelve:
    ```json
    {
        "id": 4,
        "name": "Lista ejemplo",
        "countries": [
            {
                "id": 69,
                "name": "Paraguay",
                "iso_country_code": "PY",
                "start_date": "2020-03-08T00:00:00-03:00",
                "offset": 46,
                "strategy": "No Analizado"
            },
            {
                "id": 89,
                "name": "Chile",
                "iso_country_code": "CL",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41,
                "strategy": "No Analizado"
            }
        ],
        "user_id": 1,
        "creation_date": "Fri Jul 10 00:00:00 ART 2020"
    }
    ```

- **GET /country-lists**:  Obtener todas las listas (administrador).

  - **Opcional: Query Params**:

    - **pageNumber**: Numero de pagina a buscar (por default traera la primera pagina, 0)
    - **pageSize**: Cantidad de items por pagina (default: 15)
    - **sortBy**: Criterio de ordenamiento (default: 'id') (id, name)
    
    #####  Enviamos:
    ```
    /country-lists?pageNumber=1&pageSize=2&sortBy=name
    ```    
    
    #####  Devuelve:
    ```json
    [
        {
            "id": 3,
            "name": "Lista 21",
            "countries": [
                {
                    "id": 27,
                    "name": "Bolivia",
                    "iso_country_code": "BO",
                    "start_date": "2020-03-11T00:00:00-03:00",
                    "offset": 49,
                    "strategy": "No Analizado"
                }
            ],
            "user_id": 2
        }
    ]
    ```

- **GET /country-lists/{country-list-id}**:  Obtener una lista en particular (usuario y administrador).

    #####  Devuelve:
    ```json
    {
        "id": 5,
        "name": "Otra lista ejemplo",
        "countries": [
            {
                "id": 11,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41,
                "strategy": "No Analizado"
            },
            {
                "id": 27,
                "name": "Bolivia",
                "iso_country_code": "BO",
                "start_date": "2020-03-11T00:00:00-03:00",
                "offset": 49,
                "strategy": "No Analizado"
            }
        ],
        "user_id": 4
    }
    ```

- **PUT /country-lists/{country-list-id}**:  Modificar una lista que me pertenece (usuario).
- *NOTA: Un usuario no puede modificar el nombre de una lista con el mismo nombre de otra de sus listas, el nombre es case insensitive. Si intenta modificarla el enpoint responde: Status 409 Conflict - Country List with same name already exists*

    #####  Enviar:
    ```json
    {
        "name": "Lista ejemplo modificada",
        "countries": 
        [
            {
                "iso_country_code": "AR"
            },
            {
                "iso_country_code": "BO"
            }
        ]
    }
    ```
  
    #####  Devuelve:
    ```json
    {
        "id": 4,
        "name": "Lista ejemplo modificada",
        "countries": [
            {
                "id": 27,
                "name": "Bolivia",
                "iso_country_code": "BO",
                "start_date": "2020-03-11T00:00:00-03:00",
                "offset": 49,
                "strategy": "No Analizado"
            },
            {
                "id": 11,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41,
                "strategy": "No Analizado"
            }
        ],
        "user_id": 4
    }
    ```

- **DELETE /country-lists/{id}**:  Eliminar una lista en particular (usuario).

- **POST /country-lists/{country-list-id}/countries/{country-id}**:  Agrega un país a una lista en particular.

    #####  Devuelve:
    ```json
    {
            "id": 4,
            "name": "Lista ejemplo",
            "countries": [
                {
                    "id": 11,
                    "name": "Argentina",
                    "iso_country_code": "AR",
                    "start_date": "2020-03-03T00:00:00-03:00",
                    "offset": 41,
                    "strategy": "No Analizado"
                },
                {
                    "id": 27,
                    "name": "Bolivia",
                    "iso_country_code": "BO",
                    "start_date": "2020-03-11T00:00:00-03:00",
                    "offset": 49,
                    "strategy": "No Analizado"
                },
                {
                    "id": 1,
                    "name": "Afganistán",
                    "iso_country_code": "AF",
                    "start_date": "2020-02-24T00:00:00-03:00",
                    "offset": 33,
                    "strategy": "No Analizado"
                }
            ],
            "user_id": 4
    }
    ```

- **GET /list-compare**: Selecciona 2 listas diferentes y devuelve una lista con los países en común (administrador).
  
  - **Query Params**:
  - **id1**: Id de la primera lista a comparar.
  - **id2**: Id de la segunda lista a comparar.

    #####  Devuelve:
    ```json
    [
        {
            "id": 27,
            "name": "Bolivia",
            "iso_country_code": "BO",
            "start_date": "2020-03-11T00:00:00-03:00",
            "offset": 49,
            "strategy": "No Analizado"
        }
    ]
    ```

### Estadísticas
- **GET /countries/{id}/stats**: Obtiene estadísticas de un país.
- Response:
```json
{
    "country": {
        "id": 1,
        "name": "Argentina",
        "iso_country_code": "AR",
        "start_date": "2020-04-21T16:43:26.914989-03:00"
    },
    "statistics": {
        "confirmed": 10,
        "deaths": 1,
        "recovered": 1
    }
}
```
- **GET /country-lists/{id}/stats**: Obtiene estadísticas del listado de países.
- Response:
```json
{
    "id": 1,
    "name": "Lista 11",
    "country_statistics_list": [
        {
            "country": {
                "id": 1,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41
            },
            "statistics": {
                "confirmed": 28764,
                "deaths": 785,
                "recovered": 8743
            }
        },
        {
            "country": {
                "id": 2,
                "name": "Brasil",
                "iso_country_code": "BR",
                "start_date": "2020-02-26T00:00:00-03:00",
                "offset": 35
            },
            "statistics": {
                "confirmed": 828810,
                "deaths": 41828,
                "recovered": 445123
            }
        },
        {
            "country": {
                "id": 3,
                "name": "USA",
                "iso_country_code": "US",
                "start_date": "2020-01-22T00:00:00-03:00",
                "offset": 0
            },
            "statistics": {
                "confirmed": 2048986,
                "deaths": 114669,
                "recovered": 547386
            }
        }
    ]
}
```
- **GET /country-lists/{id}/plot**:  Obtiene los datos para generar un gráfico de comparación de los países que conforman una lista.
  - **Query Params**:
    - **d**: Es el Offset numérico (Si no se indica el parámetro su valor default es cero)
        - ***d=X < 0 : Recupera las estadísticas de los últimos X días de los países de la lista***
        - ***d=X = 0 : Recupera todas las estadísticas de los países de la lista***
        - ***d=X > 0 : Recupera las estadísticas desde el día X de infección de los países de la lista***

- **Ej: Estadísticas desde el día 100 de infección**
- GET /country-lists/1/plot?d=100
- Response:
```json
{
    "id": 1,
    "name": "Lista 11",
    "country_plot_list": [
        {
            "country": {
                "id": 3,
                "name": "USA",
                "iso_country_code": "US",
                "start_date": "2020-01-22T00:00:00-03:00",
                "offset": 0
            },
            "statistics_day_list": [
                {
                    "date": "2020-04-30T00:00:00-03:00",
                    "confirmed": 1070568,
                    "deaths": 63089,
                    "recovered": 153947
                },
                {
                    "date": "2020-05-01T00:00:00-03:00",
                    "confirmed": 1104661,
                    "deaths": 65040,
                    "recovered": 164015
                },			
                {
                  ...
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 2048986,
                    "deaths": 114669,
                    "recovered": 547386
                }
            ]
        },
        {
            "country": {
                "id": 2,
                "name": "Brasil",
                "iso_country_code": "BR",
                "start_date": "2020-02-26T00:00:00-03:00",
                "offset": 35
            },
            "statistics_day_list": [
                {
                    "date": "2020-06-04T00:00:00-03:00",
                    "confirmed": 614941,
                    "deaths": 34021,
                    "recovered": 254963
                },
                {
                    "date": "2020-06-05T00:00:00-03:00",
                    "confirmed": 645771,
                    "deaths": 35026,
                    "recovered": 266940
                },
                {
                  ...
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 828810,
                    "deaths": 41828,
                    "recovered": 445123
                }
            ]
        },
        {
            "country": {
                "id": 1,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41
            },
            "statistics_day_list": [
                {
                    "date": "2020-06-10T00:00:00-03:00",
                    "confirmed": 25987,
                    "deaths": 735,
                    "recovered": 7991
                },
                {
                    "date": "2020-06-11T00:00:00-03:00",
                    "confirmed": 27373,
                    "deaths": 765,
                    "recovered": 8332
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 28764,
                    "deaths": 785,
                    "recovered": 8743
                }
            ]
        }
    ]
}
```
- **Ej: Estadísticas de los últimos 2 días**
- GET /country-lists/1/plot?d=-2
- Response:
```json
{
    "id": 1,
    "name": "Lista 11",
    "country_plot_list": [
        {
            "country": {
                "id": 1,
                "name": "Argentina",
                "iso_country_code": "AR",
                "start_date": "2020-03-03T00:00:00-03:00",
                "offset": 41
            },
            "statistics_day_list": [
                {
                    "date": "2020-06-11T00:00:00-03:00",
                    "confirmed": 27373,
                    "deaths": 765,
                    "recovered": 8332
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 28764,
                    "deaths": 785,
                    "recovered": 8743
                }
            ]
        },
        {
            "country": {
                "id": 2,
                "name": "Brasil",
                "iso_country_code": "BR",
                "start_date": "2020-02-26T00:00:00-03:00",
                "offset": 35
            },
            "statistics_day_list": [
                {
                    "date": "2020-06-11T00:00:00-03:00",
                    "confirmed": 802828,
                    "deaths": 40919,
                    "recovered": 429965
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 828810,
                    "deaths": 41828,
                    "recovered": 445123
                }
            ]
        },
        {
            "country": {
                "id": 3,
                "name": "USA",
                "iso_country_code": "US",
                "start_date": "2020-01-22T00:00:00-03:00",
                "offset": 0
            },
            "statistics_day_list": [
                {
                    "date": "2020-06-11T00:00:00-03:00",
                    "confirmed": 2023590,
                    "deaths": 113823,
                    "recovered": 540292
                },
                {
                    "date": "2020-06-12T00:00:00-03:00",
                    "confirmed": 2048986,
                    "deaths": 114669,
                    "recovered": 547386
                }
            ]
        }
    ]
}
```

- **GET /list-info**: Obtiene la cantidad total de listas registradas en el sistema (como administrador).
  - **Query Params**:
    - **d**: Es el Offset numérico(Si no se indica el parámetro su valor default es -1)
        - ***d=X = -1 : Cantdad de listas creadas en todo el sistema***
        - ***d=X = 0 : Cantdad de listas creadas el día de hoy***
        - ***d=X > 0 : Cantidad de listas creadas en los últimos X días***
    - Ejemplos de uso:
        * d=0 - En el día de hoy
        * d=3 - En los últimos 3 días
        * d=7 - En la última semana
        * d=30 - En el último mes (últimos 30 días)

- **Ej: Listas creadas en los últimos 12 días**: GET /list-info?d=12

    #####  Devuelve:
    ```json
    {
        "count": 3,
        "last_days": 12,
        "start_date": "2020-06-01T03:00:00.000+0000",
        "end_date": "2020-06-13T03:00:00.000+0000"
    }
    ```

- **POST /country-lists/plot**:  Endpoint para plotear una lista de máximo 4 países.
    Sólo se tiene en cuenta la lista de países enviada en el body y sus Iso Country Code.
  
    #####  Enviar:
    ```json
    {
        "name": "Lista Estrategia",
        "countries": 
        [
            {
                "iso_country_code": "AR"
            },
            {
                "iso_country_code": "BO"
            },
            {
                "iso_country_code": "PY"
            },
            {
                "iso_country_code": "UY"
            }
        ]
    }
    ```

    #####  Devuelve:
    ```json
    {
        "id": 99999,
        "name": "StrategyPlot",
        "country_plot_list": [
            {
                "country": {
                    "id": 72,
                    "name": "Paraguay",
                    "iso_country_code": "PY",
                    "start_date": "2020-03-08T00:00:00-03:00",
                    "offset": 46,
                    "strategy": "No Analizado"
                },
                "statistics_day_list": [
                    {
                        "date": "2020-01-22T00:00:00-03:00",
                        "confirmed": 0,
                        "deaths": 0,
                        "recovered": 0
                    },
                    [...]
    ```
  