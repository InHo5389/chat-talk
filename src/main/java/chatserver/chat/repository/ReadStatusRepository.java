package chatserver.chat.repository;

import chatserver.chat.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
}
