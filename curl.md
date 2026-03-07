curl -X POST http://localhost:8080/tasks -H 'Content-Type: application/json' -d '{"description": "Update documentation", "finished": false}' -v | python3 -m json.tool

curl -X GET http://localhost:8080/tasks -H 'Content-Type: application/json' -v | python3 -m json.tool

curl -X PATCH http://localhost:8080/tasks/1 -H 'Content-Type: application/json' -d '{"description": "Endpoint PATCH /tasks/:id", "finished": false}' -v | python3 -m json.tool

curl -X DELETE http://localhost:8080/tasks/2 -H 'Content-Type: application/json' -v | python3 -m json.tool

curl -X GET http://localhost:8080/tasks/1 -v | python3 -m json.tool