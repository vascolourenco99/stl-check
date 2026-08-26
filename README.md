# stl-check

Inspects binary STL files: triangle count and bounding box in mm.

## Run

```bash
./mvnw spring-boot:run
curl -F file=@case.stl http://localhost:8080/stl
```

```json
{"triangles":482301,"minX":-39.1,"maxX":39.1,"...":"..."}
```

## Test

```bash
./mvnw test
```

Binary STL only — ASCII files are rejected.
