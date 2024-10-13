package com.github.nnbros.rtp.gateway.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "gateway")
@NoArgsConstructor
public class User {
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "username")
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", insertable = false)
	private Status status;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime created;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updated;

	@Column(name = "last_action")
	private String lastAction;

	@Column(name = "last_action_timestamp", insertable = false)
	private LocalDateTime lastActionTime;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
