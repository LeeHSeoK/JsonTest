package org.zerock.jsontest.repository.board;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.jsontest.domain.board.Board;

import java.util.Optional;
//어떤 entity인가와 primarykey가 되는 data type를 상속받는다.
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch{
//    @Query(value = "select now()", nativeQuery = true)
//    String getTime();

    //select가 끝나면 세션 완료되서 다음 select가 안된다. board에 있는 bno를 select하고 bno를 이용해서 imageset에 파일을 select하고 싶지만
    //bno를 찾고 나면 한 세션이 완료되어 닫히는데 해당 어노테이션을 활용해서 imageset을 할때만 bno를 활용해서 select해올 수 있게 해준다.
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(Long bno);
}
