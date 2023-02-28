package com.goit.notes.note;

import com.goit.notes.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="notes")
public class Note {
    @Id
    @Column(name="note_id")
    private String noteId;

    @Column(length = 100)
    private String title;

    @Column(length = 10000)
    private String content;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ENotePrivacy privacy;
}
