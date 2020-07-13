package merikbest.sarafan.controller;

/*
https://gist.github.com/drucoder/a1d8576e1d15be38aae5bac3f914b874

ЗАПРОС В CHROME:
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

import com.fasterxml.jackson.annotation.JsonView;
import merikbest.sarafan.domain.Message;
import merikbest.sarafan.domain.Views;
import merikbest.sarafan.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @PutMapping("{id}")
    public Message update (
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message
    ) {
        //скопировать все поля из message в messageFromDb, кроме "id"
        BeanUtils.copyProperties(message, messageFromDb, "id");

        return messageRepository.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id")  Message message) {
        messageRepository.delete(message);
    }

    @MessageMapping("/changeMessage")
    @SendTo("/topic/activity")
    public Message change(Message message) {
        return messageRepository.save(message);
    }
}