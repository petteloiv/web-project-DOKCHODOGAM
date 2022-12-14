package com.ssafy.dockchodogam.controller;

import com.ssafy.dockchodogam.dto.game.RankerProfileResponseDto;
import com.ssafy.dockchodogam.dto.gg.WinRate;
import com.ssafy.dockchodogam.service.battle.BattleService;
import com.ssafy.dockchodogam.service.game.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gg")
public class GGController {
    private final BattleService battleService;
    private final GameService gameService;

    @GetMapping("/log/user/{page}")
    @ApiOperation(value = "내 전적 검색")
    public ResponseEntity<Map<String, Object>> searchLog(@PathVariable int page){
        Map<String, Object> map = new HashMap<>();
        map.put("BattleDto", battleService.searchLog(page));
        map.put("winRate", battleService.getWinRate());
        map.put("pickRate", battleService.getPickRate());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/log/user/{nickname}/{page}")
    @ApiOperation(value = "회원 전적 검색")
    public ResponseEntity<Map<String, Object>> searchLog(@PathVariable String nickname, @PathVariable int page){
        Map<String, Object> map = new HashMap<>();
        map.put("BattleDto", battleService.searchLog(nickname, page));
        map.put("winRate", battleService.getWinRate(nickname));
        map.put("pickRate", battleService.getPickRate(nickname));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/log/moster/{monster_id}/{page}")
    @ApiOperation(value = "유저 & 독초몬 별 통계")
    public ResponseEntity<Map<String, Object>> searchMonster(@PathVariable Long monster_id, @PathVariable int page){
        Map<String, Object> map = new HashMap<>();
        map.put("monsterDto", gameService.getMonsterInfo(monster_id));
        map.put("battleLog", battleService.searchLog(monster_id, page));
        map.put("winRate", battleService.getWinRate(monster_id));
        map.put("totalWinRate", battleService.getTotalWinRate(monster_id));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/monster/ranking")
    @ApiOperation(value = "전체 독초몬 승률 랭킹")
    public ResponseEntity<List<WinRate>> getMonsterRanking() {
        return new ResponseEntity<>(battleService.getMonsterRanking(), HttpStatus.OK);
    }

    @GetMapping("/ranking")
    @ApiOperation(value = "랭킹 전체 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = RankerProfileResponseDto.class)
    })
    public ResponseEntity<?> getRanking(Pageable pageable){
        // 랭킹 전체 조회
        return new ResponseEntity<>(gameService.getRanker(pageable), HttpStatus.OK);
    }
}
