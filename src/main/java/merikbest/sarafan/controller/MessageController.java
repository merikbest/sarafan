package merikbest.sarafan.controller;

/*
https://gist.github.com/drucoder/a1d8576e1d15be38aae5bac3f914b874

ЗАПРОС В CHTOME:
 fetch('/message', { method: 'POST', headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ text: 'Fourth message' })}).then(console.log)
ГДЕ:
 fetch() - метод который делает ассинхронный запрос
 '/message' - УРЛ по которому делаем запрос

 НАБОР ПАРАМЕТРОВ:
 method: 'POST' - запрос POST
 headers: {'Content-Type': 'application/json'} - необходимо передать header(application/json) иначе сервер отклонит запрос
 body: JSON.stringify({ text: 'Fourth message' })}) - наш JSON который содержит сообшение (эмулируем пользовательчкий ввод)

 метод fetch() возврашает "промис" это ассинзронный объект который после выполнения возврашает поток в метод then(),
 в котором лежит console.log (вывод в консоль) можно заменить анонимной функцией .then(result => console.log(result))






 */

import merikbest.sarafan.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {
    private int counter = 4;
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{put("id", "1"); put("text", "First message"); }});
        add(new HashMap<String, String>() {{put("id", "2"); put("text", "Second message"); }});
        add(new HashMap<String, String>() {{put("id", "3"); put("text", "Third message"); }});
    }};

    private Map<String, String> getMessage(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessage(id);
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> update (@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getMessage(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);
        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);
        messages.remove(message);
    }
}