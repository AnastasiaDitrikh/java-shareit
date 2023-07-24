package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @ToString.Exclude
    private User requester;

    private LocalDateTime created;

    public ItemRequest(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}