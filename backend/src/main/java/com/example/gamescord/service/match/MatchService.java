package com.example.gamescord.service.match;

import com.example.gamescord.domain.Gamemate;
import com.example.gamescord.domain.Match;
import com.example.gamescord.domain.User;
import com.example.gamescord.dto.match.MatchRequestDTO;
import com.example.gamescord.dto.match.MatchResponseDTO;
import com.example.gamescord.dto.match.MatchStatusUpdateRequestDTO;
import com.example.gamescord.repository.UserRepository;
import com.example.gamescord.repository.gamemate.GameMateRepository;
import com.example.gamescord.repository.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final GameMateRepository gameMateRepository;

    @Transactional
    public MatchResponseDTO requestMatch(Long requesterId, MatchRequestDTO requestDto) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("요청자 정보를 찾을 수 없습니다."));

        Gamemate gamemate = gameMateRepository.findGamemateByUsersId(requestDto.getOrderedUserId(), requestDto.getGameId());
        if (gamemate == null) {
            throw new IllegalArgumentException("해당 유저는 이 게임의 게임메이트로 등록되어 있지 않습니다.");
        }

        User gamematePlayer = gamemate.getUsers();
        Long price = gamemate.getPrice();

        if (requester.getId().equals(gamematePlayer.getId())) {
            throw new IllegalArgumentException("자기 자신에게 매칭을 요청할 수 없습니다.");
        }

        if (requester.getPoint() < price) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        requester.setPoint(requester.getPoint() - price);
        userRepository.save(requester);

        Match newMatch = new Match();
        newMatch.setUsers(requester);
        newMatch.setOrderUsersId(requester.getId());
        newMatch.setOrderedUsersId(gamematePlayer.getId());
        newMatch.setOrdersGameId(requestDto.getGameId());
        newMatch.setOrderStatus("PENDING");

        matchRepository.saveMatch(newMatch);

        return MatchResponseDTO.fromEntity(newMatch);
    }

    @Transactional
    public MatchResponseDTO updateMatchStatus(Long matchId, MatchStatusUpdateRequestDTO requestDto, Long currentUserId) {
        Match match = Optional.ofNullable(matchRepository.findById(matchId))
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));

        if (!match.getOrderedUsersId().equals(currentUserId)) {
            throw new IllegalArgumentException("매칭 상태를 변경할 권한이 없습니다.");
        }

        if (!"PENDING".equals(match.getOrderStatus())) {
            throw new IllegalStateException("대기 중인 매칭만 상태를 변경할 수 있습니다.");
        }

        String newStatus = requestDto.getStatus().toUpperCase();

        if ("ACCEPTED".equals(newStatus)) {
            match.setOrderStatus("ACCEPTED");
        } else if ("DECLINED".equals(newStatus)) {
            match.setOrderStatus("DECLINED");

            User requester = userRepository.findById(match.getOrderUsersId())
                    .orElseThrow(() -> new IllegalStateException("요청자 정보를 찾을 수 없습니다."));

            Gamemate gamemate = gameMateRepository.findGamemateByUsersId(match.getOrderedUsersId(), match.getOrdersGameId());
            if (gamemate == null) {
                throw new IllegalStateException("게임메이트 정보를 찾을 수 없습니다.");
            }
            Long price = gamemate.getPrice();

            requester.setPoint(requester.getPoint() + price);
            userRepository.save(requester);
        } else {
            throw new IllegalArgumentException("잘못된 상태 값입니다: " + newStatus);
        }

        matchRepository.saveMatch(match);
        return MatchResponseDTO.fromEntity(match);
    }
}