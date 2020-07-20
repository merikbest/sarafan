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
import merikbest.sarafan.dto.EventType;
import merikbest.sarafan.dto.ObjectType;
import merikbest.sarafan.domain.Message;
import merikbest.sarafan.domain.Views;
import merikbest.sarafan.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import merikbest.sarafan.util.WsSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepository messageRepository;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepository messageRepository, WsSender wsSender) {
        this.messageRepository = messageRepository;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
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
        Message updatedMessage = messageRepository.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update (
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message
    ) {
        //скопировать все поля из message в messageFromDb, кроме "id"
        BeanUtils.copyProperties(message, messageFromDb, "id");

        Message updatedMessage = messageRepository.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id")  Message message) {
        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }
}