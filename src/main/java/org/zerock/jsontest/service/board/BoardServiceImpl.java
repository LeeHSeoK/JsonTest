package org.zerock.jsontest.service.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.dto.board.*;
import org.zerock.jsontest.repository.board.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public void incrementViewCount(Long bno) {
        boardRepository.incrementViewCount(bno);
    }

    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = dtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        BoardDTO boardDTO = entityToDTO(board);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.change(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getName(),
                boardDTO.getXaxis(), boardDTO.getYaxis(), boardDTO.getPlaceName());
        // 첨부파일의 수정
        board.clearImage();
        if(boardDTO.getFileNames() != null){
            for(String fileName : boardDTO.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getSortedPageable();

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        // likeCount 값 추가해서 같이 담는다.
        List<BoardListAllDTO> dtoList = result.getContent().stream().map(boardDTO -> {
            Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow();
            boardDTO.setLikeCount(board.getLikeCount());
            return boardDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
