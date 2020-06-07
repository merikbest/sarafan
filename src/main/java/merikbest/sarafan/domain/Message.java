package merikbest.sarafan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = {"id"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //    @JsonView(Views.IdName.class) ИНТЕРФЕЙС МАРКЕР ДЛЯ ОТОБРАЖЕНИЯ ПОЛЕЙ
    // когда мы будет отдавать Message через Id с указанием Views.IdName,
    // мы будем видеть только поля помеченые Views.IdName
    //а когда будем отдавать филды помеченые Views.FullMessage, тогда мы будем видеть филды Views.FullMessage и Views.IdName
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;
}
