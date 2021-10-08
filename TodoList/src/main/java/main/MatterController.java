package main;

import main.model.MatterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.model.Matter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todolist")
public class MatterController {

    @Autowired
    private MatterRepository matterRepository;

    @PostMapping(value = "/")
    public int add(Matter matter) {
        Matter newMatter = matterRepository.save(matter);
        return newMatter.getId();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteMatter(@PathVariable int id) {
        if (!matterRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        matterRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Matter> changeMatter(@PathVariable(value = "id") Integer matterId,
                                               @RequestBody Matter matterDetails){
        Optional <Matter> matter = matterRepository.findById(matterId);
        if (matter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        matter.get().setName(matterDetails.getName());
        matter.get().setAuthor(matterDetails.getAuthor());
        matterRepository.save(matter.get());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @DeleteMapping("/")
    public void deleteAllMatters() {
        matterRepository.deleteAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity getmatter(@PathVariable int id) {
        Optional<Matter> matter = matterRepository.findById(id);
        if (matter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(matter, HttpStatus.OK);
    }


    @GetMapping("/")
    public List<Matter> getAllmatters() {
        Iterable<Matter> matterIterable = matterRepository.findAll();
        ArrayList<Matter> matters = new ArrayList<>();
        for (Matter matter : matterIterable){
            matters.add(matter);
        }
        return matters;
    }
}
