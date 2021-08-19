-- 活动表
CREATE TABLE `tb_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) NOT NULL COMMENT '活动名称',
  `description` text NULL COMMENT '活动信息, 描述信息',
  `status` char(16) NOT NULL DEFAULT 'active' COMMENT '活动状态, active-有效, invalid-无效',
  `draw_operation_type` char(32) NOT NULL DEFAULT 'drawEvent' COMMENT '抽奖执行方式, drawEvent-按照资格, drawTimes-按照次数',
  `start_time` timestamp NOT NULL COMMENT '活动开始时间',
  `end_time` timestamp NOT NULL COMMENT '活动结束时间',
  `draw_start_time` timestamp NOT NULL COMMENT '抽奖开始时间',
  `draw_end_time` timestamp NOT NULL COMMENT '抽奖结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动表';


-- 奖池表
CREATE TABLE `tb_jackpot` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='抽奖奖池表';


-- 活动参与者表
CREATE TABLE `tb_participant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qualification_id` int(11) NOT NULL COMMENT '资格Id',
  `user_id` bigint(11) NOT NULL COMMENT '用户Id',
  `current_chance_number` int(11) NOT NULL DEFAULT 0 COMMENT '当前资格剩余抽奖次数',
  `total_chance_number` int(11) NOT NULL DEFAULT 0 COMMENT '当前资格总抽奖次数',
  `status` char(16) NOT NULL DEFAULT '' COMMENT '当前资格总抽奖次数',
  `act_id` int(11) NOT NULL COMMENT '活动Id-冗余字段',
  `priority` int(11) NOT NULL DEFAULT 0 COMMENT '资格消耗优先级-冗余字段',
  `jackpot_id` int(11) NULL COMMENT '奖池Id-冗余字段',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动参与者表';


-- 资格表
CREATE TABLE `tb_qualification` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `act_id` int(11) NOT NULL COMMENT '活动Id',
  `event_key` varchar(64) NOT NULL COMMENT '资格事件名称',
  `jackpot_id` int(11) NOT NULL COMMENT '奖池Id',
  `start_time` timestamp NOT NULL COMMENT '资格开始时间',
  `end_time` timestamp NOT NULL COMMENT '资格结束时间',
  `dependents` varchar(64) NULL COMMENT '当前资格所依赖的资格, 资格Id用,隔开',
  `chance_number` int(11) NOT NULL DEFAULT 1 COMMENT '满足资格单次下发抽奖次数',
  `single_limit` int(11) NULL COMMENT '单人获取资格上限',
  `single_daily_limit` int(11) NULL COMMENT '单人每日获取资格上限',
  `total_limit` int(11) NULL COMMENT '累计获取资格上限',
  `total_daily_limit` int(11) NULL COMMENT '累计每日获取资格上限',
  `priority` int(11) NOT NULL DEFAULT 0 COMMENT '用户Id',
  `ext` varchar(255) NULL COMMENT '补充条件-json格式',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='资格表';


-- 抽奖流水记录
CREATE TABLE `tb_record_lottery` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qualification_id` int(11) NOT NULL COMMENT '本次抽奖消耗的资格Id',
  `user_id` bigint(11) NOT NULL COMMENT '用户Id',
  `jackpot_id` int(11) NOT NULL COMMENT '奖池Id',
  `prize_id` int(11) NOT NULL COMMENT '奖品Id',
  `act_id` int(11) NOT NULL COMMENT '活动Id-冗余字段',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='抽奖流水记录表';


-- 资格流水记录
CREATE TABLE `tb_record_lottery` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qualification_id` int(11) NOT NULL COMMENT '资格Id',
  `user_id` bigint(11) NOT NULL COMMENT '用户Id',
  `chance_number` int(11) NOT NULL COMMENT '本次资格下发数量',
  `source` varchar(64) NOT NULL COMMENT '来源记录',
  `operation` char(32) NOT NULL COMMENT '操作类型',
  `act_id` int(11) NOT NULL COMMENT '活动Id-冗余字段',
  `event_key` varchar(64) NOT NULL COMMENT '资格事件名称-冗余字段',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='资格流水记录表';