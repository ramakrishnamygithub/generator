package com.dynamicapp.generator.Controller;

import com.dynamicapp.generator.entity.EntityData;
import com.dynamicapp.generator.entity.EntityMetadata;
import com.dynamicapp.generator.entity.FieldMetadata;
import com.dynamicapp.generator.repository.EntityDataRepository;
import com.dynamicapp.generator.repository.EntityMetadataRepository;
import com.dynamicapp.generator.repository.FieldMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DynamicAppGeneratorController {
    @Autowired
    private EntityMetadataRepository entityRepo;
    @Autowired
    private FieldMetadataRepository fieldRepo;
    @Autowired
    private EntityDataRepository dataRepo;

    @PostMapping("/entities")
    public ResponseEntity<String> createEntity(@RequestBody Map<String, Object> req) {
        String entityName = ((String) req.get("entity")).toLowerCase();
        List<String> fields = (List<String>) req.get("fields");
        entityRepo.findByName(entityName).ifPresent((entityMetadata)->{
                   dataRepo.deleteByEntity(entityMetadata);
                   fieldRepo.deleteByEntity_Id(entityMetadata.getId());
                   entityRepo.deleteById(entityMetadata.getId());
                }
                );

            EntityMetadata e = new EntityMetadata();
            e.setName(entityName);
          entityRepo.save(e);

        for (String field : fields) {
            FieldMetadata f = new FieldMetadata();
            f.setEntity(e);
            f.setFieldName(field);
            f.setFieldType(field.toLowerCase().matches(".*(marks|number|roll|price|attendance|stock|age).*") ? "number" : "text");
            fieldRepo.save(f);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Entity created");
    }

    @GetMapping("/entities/{name}")
    public ResponseEntity<List<FieldMetadata>> getEntityFields(@PathVariable String name) {
        EntityMetadata entity = entityRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
        List<FieldMetadata> fields = fieldRepo.findByEntity(entity);
        return ResponseEntity.ok(fields);
    }

    @PostMapping("/entities/{name}/submit")
    public ResponseEntity<String> submitData(@PathVariable String name, @RequestBody Map<String, String> data) {
        EntityMetadata entity = entityRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
        Long newRecordId = System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            FieldMetadata field = fieldRepo.findByEntity(entity).stream()
                    .filter(f -> f.getFieldName().equalsIgnoreCase(entry.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field not found: " + entry.getKey()));

            EntityData row = new EntityData();
            row.setEntity(entity);
            row.setField(field);
            row.setRecordId(newRecordId);
            row.setDataValue(entry.getValue());
            dataRepo.save(row);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Submitted");
    }

    @GetMapping("/entities/{name}/records")
    public ResponseEntity<List<Map<String, String>>> getRecords(@PathVariable String name) {
        EntityMetadata entity = entityRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
        List<EntityData> all = dataRepo.findByEntity(entity);

        Map<Long, Map<String, String>> grouped = new HashMap<>();
        for (EntityData d : all) {
            grouped.computeIfAbsent(d.getRecordId(), k -> new HashMap<>())
                    .put(d.getField().getFieldName(), d.getDataValue());
        }
        return ResponseEntity.ok(new ArrayList<>(grouped.values()));
    }

    @DeleteMapping("/deleteApps")
    public ResponseEntity<Void> deleteAllForms() {
        dataRepo.deleteAll();
        fieldRepo.deleteAll();
        entityRepo.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
