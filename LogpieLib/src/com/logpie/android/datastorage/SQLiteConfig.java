package com.logpie.android.datastorage;

public class SQLiteConfig
{
    /**
     * SQL for creating tables
     */

    /* Table 'city' */
    public final static String SQL_CREATE_CITY_TABLE = "CREATE TABLE city (cid INTEGER PRIMARY KEY, city TEXT NOT NULL, grade INTEGER NOT NULL, province TEXT NOT NULL);";
    /**
     * The maximum number of SQLite insert operation is 500.
     */
    public final static String SQL_INSERT_PART_1_CITY_TABLE = "INSERT INTO city (cid, city, grade, province) VALUES (1, '北京市', 0, '直辖市'), (2, '上海市', 0, '直辖市'), (3, '天津市', 0, '直辖市'), (4, '重庆市', 0, '直辖市'), (5, '石家庄市', 1, '河北省'), (6, '唐山市', 1, '河北省'), (7, '秦皇岛市', 1, '河北省'), (8, '邯郸市', 1, '河北省'), (9, '邢台市', 1, '河北省'), (10, '保定市', 1, '河北省'), (11, '张家口市', 1, '河北省'), (12, '承德市', 1, '河北省'), (13, '沧州市', 1, '河北省'), (14, '廊坊市', 1, '河北省'), (15, '衡水市', 1, '河北省'), (16, '辛集市', 2, '河北省'), (17, '藁城市', 2, '河北省'), (18, '晋州市', 2, '河北省'), (19, '新乐市', 2, '河北省'), (20, '鹿泉市', 2, '河北省'), (21, '遵化市', 2, '河北省'), (22, '丰南市', 2, '河北省'), (23, '迁安市', 2, '河北省'), (24, '武安市', 2, '河北省'), (25, '南宫市', 2, '河北省'), (26, '沙河市', 2, '河北省'), (27, '涿州市', 2, '河北省'), (28, '定州市', 2, '河北省'), (29, '安国市', 2, '河北省'), (30, '高碑店市', 2, '河北省'), (31, '泊头市', 2, '河北省'), (32, '任丘市', 2, '河北省'), (33, '黄骅市', 2, '河北省'), (34, '河间市', 2, '河北省'), (35, '霸州市', 2, '河北省'), (36, '三河市', 2, '河北省'), (37, '冀州市', 2, '河北省'), (38, '深州市', 2, '河北省'), (39, '太原市', 1, '山西省'), (40, '大同市', 1, '山西省'), (41, '阳泉市', 1, '山西省'), (42, '长治市', 1, '山西省'), (43, '晋城市', 1, '山西省'), (44, '朔州市', 1, '山西省'), (45, '古交市', 2, '山西省'), (46, '潞城市', 2, '山西省'), (47, '高平市', 2, '山西省'), (48, '忻州市', 2, '山西省'), (49, '原平市', 2, '山西省'), (50, '孝义市', 2, '山西省'), (51, '离石市', 2, '山西省'), (52, '汾阳市', 2, '山西省'), (53, '榆次市', 2, '山西省'), (54, '介休市', 2, '山西省'), (55, '临汾市', 2, '山西省'), (56, '侯马市', 2, '山西省'), (57, '霍州市', 2, '山西省'), (58, '运城市', 2, '山西省'), (59, '永济市', 2, '山西省'), (60, '河津市', 2, '山西省'), (61, '呼和浩特市', 1, '内蒙古自治区'), (62, '包头市', 1, '内蒙古自治区'), (63, '乌海市', 1, '内蒙古自治区'), (64, '赤峰市', 1, '内蒙古自治区'), (65, '通辽市', 1, '内蒙古自治区'), (66, '霍林郭勒市', 2, '内蒙古自治区'), (67, '海拉尔市', 2, '内蒙古自治区'), (68, '满洲里市', 2, '内蒙古自治区'), (69, '扎兰屯市', 2, '内蒙古自治区'), (70, '牙克石市', 2, '内蒙古自治区'), (71, '根河市', 2, '内蒙古自治区'), (72, '额尔古纳市', 2, '内蒙古自治区'), (73, '乌兰浩特市', 2, '内蒙古自治区'), (74, '二连浩特市', 2, '内蒙古自治区'), (75, '锡林浩特市', 2, '内蒙古自治区'), (76, '集宁市', 2, '内蒙古自治区'), (77, '丰镇市', 2, '内蒙古自治区'), (78, '东胜市', 2, '内蒙古自治区'), (79, '临河市', 2, '内蒙古自治区'), (80, '沈阳市', 1, '辽宁省'), (81, '大连市', 1, '辽宁省'), (82, '鞍山市', 1, '辽宁省'), (83, '抚顺市', 1, '辽宁省'), (84, '本溪市', 1, '辽宁省'), (85, '丹东市', 1, '辽宁省'), (86, '锦州市', 1, '辽宁省'), (87, '营口市', 1, '辽宁省'), (88, '阜新市', 1, '辽宁省'), (89, '辽阳市', 1, '辽宁省'), (90, '盘锦市', 1, '辽宁省'), (91, '铁岭市', 1, '辽宁省'), (92, '朝阳市', 1, '辽宁省'), (93, '葫芦岛市', 1, '辽宁省'), (94, '新民市', 2, '辽宁省'), (95, '瓦房店市', 2, '辽宁省'), (96, '普兰店市', 2, '辽宁省'), (97, '庄河市', 2, '辽宁省'), (98, '海城市', 2, '辽宁省'), (99, '东港市', 2, '辽宁省'), (100, '凤城市', 2, '辽宁省'), (101, '凌海市', 2, '辽宁省'), (102, '北宁市', 2, '辽宁省'), (103, '盖州市', 2, '辽宁省'), (104, '大石桥市', 2, '辽宁省'), (105, '灯塔市', 2, '辽宁省'), (106, '铁法市', 2, '辽宁省'), (107, '开原市', 2, '辽宁省'), (108, '北票市', 2, '辽宁省'), (109, '凌源市', 2, '辽宁省'), (110, '兴城市', 2, '辽宁省'), (111, '长春市', 1, '吉林省'), (112, '吉林市', 1, '吉林省'), (113, '四平市', 1, '吉林省'), (114, '辽源市', 1, '吉林省'), (115, '通化市', 1, '吉林省'), (116, '白山市', 1, '吉林省'), (117, '松原市', 1, '吉林省'), (118, '白城市', 1, '吉林省'), (119, '九台市', 2, '吉林省'), (120, '榆树市', 2, '吉林省'), (121, '德惠市', 2, '吉林省'), (122, '蛟河市', 2, '吉林省'), (123, '桦甸市', 2, '吉林省'), (124, '舒兰市', 2, '吉林省'), (125, '磐石市', 2, '吉林省'), (126, '公主岭市', 2, '吉林省'), (127, '双辽市', 2, '吉林省'), (128, '梅河口市', 2, '吉林省'), (129, '集安市', 2, '吉林省'), (130, '临江市', 2, '吉林省'), (131, '洮南市', 2, '吉林省'), (132, '大安市', 2, '吉林省'), (133, '延吉市', 2, '吉林省'), (135, '图们市', 2, '吉林省'), (136, '敦化市', 2, '吉林省'), (137, '珲春市', 2, '吉林省'), (138, '龙井市', 2, '吉林省'), (139, '和龙市', 2, '吉林省'), (140, '哈尔滨市', 1, '黑龙江省'), (141, '齐齐哈尔市', 1, '黑龙江省'), (142, '鸡西市', 1, '黑龙江省'), (143, '鹤岗市', 1, '黑龙江省'), (144, '双鸭山市', 1, '黑龙江省'), (145, '大庆市', 1, '黑龙江省'), (146, '伊春市', 1, '黑龙江省'), (147, '佳木斯市', 1, '黑龙江省'), (148, '七台河市', 1, '黑龙江省'), (149, '牡丹江市', 1, '黑龙江省'), (150, '黑河市', 1, '黑龙江省'), (151, '阿城市', 2, '黑龙江省'), (152, '双城市', 2, '黑龙江省'), (153, '尚志市', 2, '黑龙江省'), (154, '五常市', 2, '黑龙江省'), (155, '讷河市', 2, '黑龙江省'), (156, '虎林市', 2, '黑龙江省'), (157, '密山市', 2, '黑龙江省'), (158, '铁力市', 2, '黑龙江省'), (159, '同江市', 2, '黑龙江省'), (160, '富锦市', 2, '黑龙江省'), (161, '绥芬河市', 2, '黑龙江省'), (162, '海林市', 2, '黑龙江省'), (163, '宁安市', 2, '黑龙江省'), (164, '穆棱市', 2, '黑龙江省'), (165, '北安市', 2, '黑龙江省'), (166, '五大连池市', 2, '黑龙江省'), (167, '绥化市', 2, '黑龙江省'), (168, '安达市', 2, '黑龙江省'), (169, '肇东市', 2, '黑龙江省'), (170, '海伦市', 2, '黑龙江省'), (171, '南京市', 1, '江苏省'), (172, '无锡市', 1, '江苏省'), (173, '徐州市', 1, '江苏省'), (174, '常州市', 1, '江苏省'), (175, '苏州市', 1, '江苏省'), (176, '南通市', 1, '江苏省'), (177, '连云港市', 1, '江苏省'), (178, '淮阴市', 1, '江苏省'), (179, '盐城市', 1, '江苏省'), (180, '扬州市', 1, '江苏省'), (181, '镇江市', 1, '江苏省'), (182, '泰州市', 1, '江苏省'), (183, '宿迁市', 1, '江苏省'), (184, '江阴市', 2, '江苏省'), (185, '宜兴市', 2, '江苏省'), (186, '锡山市', 2, '江苏省'), (187, '新沂市', 2, '江苏省'), (188, '邳州市', 2, '江苏省'), (189, '溧阳市', 2, '江苏省'), (190, '金坛市', 2, '江苏省'), (191, '武进市', 2, '江苏省'), (192, '常熟市', 2, '江苏省'), (193, '张家港市', 2, '江苏省'), (194, '昆山市', 2, '江苏省'), (195, '吴江市', 2, '江苏省'), (196, '太仓市', 2, '江苏省'), (197, '吴县市', 2, '江苏省'), (198, '启东市', 2, '江苏省'), (199, '如皋市', 2, '江苏省'), (200, '通州市', 2, '江苏省'), (201, '海门市', 2, '江苏省'), (202, '淮安市', 2, '江苏省'), (203, '东台市', 2, '江苏省'), (204, '大丰市', 2, '江苏省'), (205, '仪征市', 2, '江苏省'), (206, '高邮市', 2, '江苏省'), (207, '江都市', 2, '江苏省'), (208, '丹阳市', 2, '江苏省'), (209, '扬中市', 2, '江苏省'), (210, '句容市', 2, '江苏省'), (211, '兴化市', 2, '江苏省'), (212, '靖江市', 2, '江苏省'), (213, '泰兴市', 2, '江苏省'), (214, '姜堰市', 2, '江苏省'), (215, '杭州市', 1, '浙江省'), (216, '宁波市', 1, '浙江省'), (217, '温州市', 1, '浙江省'), (218, '嘉兴市', 1, '浙江省'), (219, '湖州市', 1, '浙江省'), (220, '绍兴市', 1, '浙江省'), (221, '金华市', 1, '浙江省'), (222, '衢州市', 1, '浙江省'), (223, '舟山市', 1, '浙江省'), (224, '台州市', 1, '浙江省'), (225, '萧山市', 2, '浙江省'), (226, '建德市', 2, '浙江省'), (227, '富阳市', 2, '浙江省'), (228, '余杭市', 2, '浙江省'), (229, '临安市', 2, '浙江省'), (230, '余姚市', 2, '浙江省'), (231, '慈溪市', 2, '浙江省'), (232, '奉化市', 2, '浙江省'), (233, '瑞安市', 2, '浙江省'), (234, '乐清市', 2, '浙江省'), (235, '海宁市', 2, '浙江省'), (236, '平湖市', 2, '浙江省'), (237, '桐乡市', 2, '浙江省'), (238, '诸暨市', 2, '浙江省'), (239, '上虞市', 2, '浙江省'), (240, '嵊州市', 2, '浙江省'), (241, '兰溪市', 2, '浙江省'), (242, '义乌市', 2, '浙江省'), (243, '东阳市', 2, '浙江省'), (244, '永康市', 2, '浙江省'), (245, '江山市', 2, '浙江省'), (246, '温岭市', 2, '浙江省'), (247, '临海市', 2, '浙江省'), (248, '丽水市', 2, '浙江省'), (249, '龙泉市', 2, '浙江省'), (250, '合肥市', 1, '安徽省'), (251, '芜湖市', 1, '安徽省'), (252, '蚌埠市', 1, '安徽省'), (253, '淮南市', 1, '安徽省'), (254, '马鞍山市', 1, '安徽省'), (255, '淮北市', 1, '安徽省'), (256, '铜陵市', 1, '安徽省'), (257, '安庆市', 1, '安徽省'), (258, '黄山市', 1, '安徽省'), (259, '滁州市', 1, '安徽省'), (260, '阜阳市', 1, '安徽省'), (261, '宿州市', 1, '安徽省'), (262, '巢湖市', 1, '安徽省'), (263, '六安市', 1, '安徽省'), (264, '桐城市', 2, '安徽省'), (265, '天长市', 2, '安徽省'), (266, '明光市', 2, '安徽省'), (267, '亳州市', 2, '安徽省'), (268, '界首市', 2, '安徽省'), (269, '宣州市', 2, '安徽省'), (270, '宁国市', 2, '安徽省'), (271, '贵池市', 2, '安徽省'), (272, '福州市', 1, '福建省'), (273, '厦门市', 1, '福建省'), (274, '莆田市', 1, '福建省'), (275, '三明市', 1, '福建省'), (276, '泉州市', 1, '福建省'), (277, '漳州市', 1, '福建省'), (278, '南平市', 1, '福建省'), (279, '龙岩市', 1, '福建省'), (280, '福清市', 2, '福建省'), (281, '长乐市', 2, '福建省'), (282, '永安市', 2, '福建省'), (283, '石狮市', 2, '福建省'), (284, '晋江市', 2, '福建省'), (285, '南安市', 2, '福建省'), (286, '龙海市', 2, '福建省'), (287, '邵武市', 2, '福建省'), (288, '武夷山市', 2, '福建省'), (289, '建瓯市', 2, '福建省'), (290, '建阳市', 2, '福建省'), (291, '漳平市', 2, '福建省'), (292, '宁德市', 2, '福建省'), (293, '福安市', 2, '福建省'), (294, '福鼎市', 2, '福建省'), (295, '南昌市', 1, '江西省'), (296, '景德镇市', 1, '江西省'), (297, '萍乡市', 1, '江西省'), (298, '九江市', 1, '江西省'), (299, '新余市', 1, '江西省'), (300, '鹰潭市', 1, '江西省'), (301, '赣州市', 1, '江西省'), (302, '乐平市', 2, '江西省'), (303, '瑞昌市', 2, '江西省'), (304, '贵溪市', 2, '江西省'), (305, '瑞金市', 2, '江西省'), (306, '南康市', 2, '江西省'), (307, '宜春市', 2, '江西省'), (308, '丰城市', 2, '江西省'), (309, '樟树市', 2, '江西省'), (310, '高安市', 2, '江西省'), (311, '上饶市', 2, '江西省'), (312, '德兴市', 2, '江西省'), (313, '吉安市', 2, '江西省'), (314, '井冈山市', 2, '江西省'), (315, '临川市', 2, '江西省'), (316, '济南市', 1, '山东省'), (317, '青岛市', 1, '山东省'), (318, '淄博市', 1, '山东省'), (319, '枣庄市', 1, '山东省'), (320, '东营市', 1, '山东省'), (321, '烟台市', 1, '山东省'), (322, '潍坊市', 1, '山东省'), (323, '济宁市', 1, '山东省'), (324, '泰安市', 1, '山东省'), (325, '威海市', 1, '山东省'), (326, '日照市', 1, '山东省'), (327, '莱芜市', 1, '山东省'), (328, '临沂市', 1, '山东省'), (329, '德州市', 1, '山东省'), (330, '聊城市', 1, '山东省'), (331, '章丘市', 2, '山东省');";
    public final static String SQL_INSERT_PART_2_CITY_TABLE = "INSERT INTO city (cid, city, grade, province) VALUES (332, '胶州市', 2, '山东省'), (333, '即墨市', 2, '山东省'), (334, '平度市', 2, '山东省'), (335, '胶南市', 2, '山东省'), (336, '莱西市', 2, '山东省'), (337, '滕州市', 2, '山东省'), (338, '龙口市', 2, '山东省'), (339, '莱阳市', 2, '山东省'), (340, '莱州市', 2, '山东省'), (341, '蓬莱市', 2, '山东省'), (342, '招远市', 2, '山东省'), (343, '栖霞市', 2, '山东省'), (344, '海阳市', 2, '山东省'), (345, '青州市', 2, '山东省'), (346, '诸城市', 2, '山东省'), (347, '寿光市', 2, '山东省'), (348, '安丘市', 2, '山东省'), (349, '高密市', 2, '山东省'), (350, '昌邑市', 2, '山东省'), (351, '曲阜市', 2, '山东省'), (352, '兖州市', 2, '山东省'), (353, '邹城市', 2, '山东省'), (354, '新泰市', 2, '山东省'), (355, '肥城市', 2, '山东省'), (356, '文登市', 2, '山东省'), (357, '荣成市', 2, '山东省'), (358, '乳山市', 2, '山东省'), (359, '乐陵市', 2, '山东省'), (360, '禹城市', 2, '山东省'), (361, '临清市', 2, '山东省'), (362, '滨州市', 2, '山东省'), (363, '菏泽市', 2, '山东省'), (364, '郑州市', 1, '河南省'), (365, '开封市', 1, '河南省'), (366, '洛阳市', 1, '河南省'), (367, '平顶山市', 1, '河南省'), (368, '安阳市', 1, '河南省'), (369, '鹤壁市', 1, '河南省'), (370, '新乡市', 1, '河南省'), (371, '焦作市', 1, '河南省'), (372, '濮阳市', 1, '河南省'), (373, '许昌市', 1, '河南省'), (374, '漯河市', 1, '河南省'), (375, '三门峡市', 1, '河南省'), (376, '南阳市', 1, '河南省'), (377, '商丘市', 1, '河南省'), (378, '信阳市', 1, '河南省'), (379, '巩义市', 2, '河南省'), (380, '荥阳市', 2, '河南省'), (381, '新密市', 2, '河南省'), (382, '新郑市', 2, '河南省'), (383, '登封市', 2, '河南省'), (384, '偃师市', 2, '河南省'), (385, '舞钢市', 2, '河南省'), (386, '汝州市', 2, '河南省'), (387, '林州市', 2, '河南省'), (388, '卫辉市', 2, '河南省'), (389, '辉县市', 2, '河南省'), (390, '济源市', 2, '河南省'), (391, '沁阳市', 2, '河南省'), (392, '孟州市', 2, '河南省'), (393, '禹州市', 2, '河南省'), (394, '长葛市', 2, '河南省'), (395, '义马市', 2, '河南省'), (396, '灵宝市', 2, '河南省'), (397, '邓州市', 2, '河南省'), (398, '永城市', 2, '河南省'), (399, '周口市', 2, '河南省'), (400, '项城市', 2, '河南省'), (401, '驻马店市', 2, '河南省'), (402, '武汉市', 1, '湖北省'), (403, '黄石市', 1, '湖北省'), (404, '十堰市', 1, '湖北省'), (405, '宜昌市', 1, '湖北省'), (406, '襄樊市', 1, '湖北省'), (407, '鄂州市', 1, '湖北省'), (408, '荆门市', 1, '湖北省'), (409, '孝感市', 1, '湖北省'), (410, '荆州市', 1, '湖北省'), (411, '黄冈市', 1, '湖北省'), (412, '咸宁市', 1, '湖北省'), (413, '大冶市', 2, '湖北省'), (414, '丹江口市', 2, '湖北省'), (415, '枝城市', 2, '湖北省'), (416, '当阳市', 2, '湖北省'), (417, '枝江市', 2, '湖北省'), (418, '老河口市', 2, '湖北省'), (419, '枣阳市', 2, '湖北省'), (420, '宜城市', 2, '湖北省'), (421, '钟祥市', 2, '湖北省'), (422, '应城市', 2, '湖北省'), (423, '安陆市', 2, '湖北省'), (424, '广水市', 2, '湖北省'), (425, '汉川市', 2, '湖北省'), (426, '石首市', 2, '湖北省'), (427, '洪湖市', 2, '湖北省'), (428, '松滋市', 2, '湖北省'), (429, '麻城市', 2, '湖北省'), (430, '武穴市', 2, '湖北省'), (431, '赤壁市', 2, '湖北省'), (432, '恩施市', 2, '湖北省'), (433, '利川市', 2, '湖北省'), (434, '随州市', 2, '湖北省'), (435, '仙桃市', 2, '湖北省'), (436, '潜江市', 2, '湖北省'), (437, '天门市', 2, '湖北省'), (438, '长沙市', 1, '湖南省'), (439, '株洲市', 1, '湖南省'), (440, '湘潭市', 1, '湖南省'), (441, '衡阳市', 1, '湖南省'), (442, '邵阳市', 1, '湖南省'), (443, '岳阳市', 1, '湖南省'), (444, '常德市', 1, '湖南省'), (445, '张家界市', 1, '湖南省'), (446, '益阳市', 1, '湖南省'), (447, '郴州市', 1, '湖南省'), (448, '永州市', 1, '湖南省'), (449, '怀化市', 1, '湖南省'), (450, '娄底市', 1, '湖南省'), (451, '浏阳市', 2, '湖南省'), (452, '醴陵市', 2, '湖南省'), (453, '湘乡市', 2, '湖南省'), (454, '韶山市', 2, '湖南省'), (455, '耒阳市', 2, '湖南省'), (456, '常宁市', 2, '湖南省'), (457, '武冈市', 2, '湖南省'), (458, '汩罗市', 2, '湖南省'), (459, '临湘市', 2, '湖南省'), (460, '津市市', 2, '湖南省'), (461, '沅江市', 2, '湖南省'), (462, '资兴市', 2, '湖南省'), (463, '洪江市', 2, '湖南省'), (464, '冷水江市', 2, '湖南省'), (465, '涟源市', 2, '湖南省'), (466, '吉首市', 2, '湖南省'), (467, '广州市', 1, '广东省'), (468, '韶关市', 1, '广东省'), (469, '深圳市', 1, '广东省'), (470, '珠海市', 1, '广东省'), (471, '汕头市', 1, '广东省'), (472, '佛山市', 1, '广东省'), (473, '江门市', 1, '广东省'), (474, '湛江市', 1, '广东省'), (475, '茂名市', 1, '广东省'), (476, '肇庆市', 1, '广东省'), (477, '惠州市', 1, '广东省'), (478, '梅州市', 1, '广东省'), (479, '汕尾市', 1, '广东省'), (480, '河源市', 1, '广东省'), (481, '阳江市', 1, '广东省'), (482, '清远市', 1, '广东省'), (483, '东莞市', 1, '广东省'), (484, '中山市', 1, '广东省'), (485, '潮州市', 1, '广东省'), (486, '揭阳市', 1, '广东省'), (487, '云浮市', 1, '广东省'), (488, '番禺市', 2, '广东省'), (489, '花都市', 2, '广东省'), (490, '增城市', 2, '广东省'), (491, '从化市', 2, '广东省'), (492, '乐昌市', 2, '广东省'), (493, '南雄市', 2, '广东省'), (494, '潮阳市', 2, '广东省'), (495, '澄海市', 2, '广东省'), (496, '顺德市', 2, '广东省'), (497, '南海市', 2, '广东省'), (498, '三水市', 2, '广东省'), (499, '高明市', 2, '广东省'), (500, '台山市', 2, '广东省'), (501, '新会市', 2, '广东省'), (502, '开平市', 2, '广东省'), (503, '鹤山市', 2, '广东省'), (504, '恩平市', 2, '广东省'), (505, '廉江市', 2, '广东省'), (506, '雷州市', 2, '广东省'), (507, '吴川市', 2, '广东省'), (508, '高州市', 2, '广东省'), (509, '化州市', 2, '广东省'), (510, '信宜市', 2, '广东省'), (511, '高要市', 2, '广东省'), (512, '四会市', 2, '广东省'), (513, '惠阳市', 2, '广东省'), (514, '兴宁市', 2, '广东省'), (515, '陆丰市', 2, '广东省'), (516, '阳春市', 2, '广东省'), (517, '英德市', 2, '广东省'), (518, '连州市', 2, '广东省'), (519, '普宁市', 2, '广东省'), (520, '罗定市', 2, '广东省'), (521, '南宁市', 1, '广西壮族自治区'), (522, '柳州市', 1, '广西壮族自治区'), (523, '桂林市', 1, '广西壮族自治区'), (524, '梧州市', 1, '广西壮族自治区'), (525, '北海市', 1, '广西壮族自治区'), (526, '防城港市', 1, '广西壮族自治区'), (527, '钦州市', 1, '广西壮族自治区'), (528, '贵港市', 1, '广西壮族自治区'), (529, '玉林市', 1, '广西壮族自治区'), (530, '岑溪市', 2, '广西壮族自治区'), (531, '东兴市', 2, '广西壮族自治区'), (532, '桂平市', 2, '广西壮族自治区'), (533, '北流市', 2, '广西壮族自治区'), (534, '凭祥市', 2, '广西壮族自治区'), (535, '合山市', 2, '广西壮族自治区'), (536, '贺州市', 2, '广西壮族自治区'), (537, '百色市', 2, '广西壮族自治区'), (538, '河池市', 2, '广西壮族自治区'), (539, '宜州市', 2, '广西壮族自治区'), (540, '海口市', 1, '海南省'), (541, '三亚市', 1, '海南省'), (542, '通什市', 2, '海南省'), (543, '琼海市', 2, '海南省'), (544, '儋州市', 2, '海南省'), (545, '琼山市', 2, '海南省'), (546, '文昌市', 2, '海南省'), (547, '万宁市', 2, '海南省'), (548, '东方市', 2, '海南省'), (549, '成都市', 1, '四川省'), (550, '自贡市', 1, '四川省'), (551, '攀枝花市', 1, '四川省'), (552, '泸州市', 1, '四川省'), (553, '德阳市', 1, '四川省'), (554, '绵阳市', 1, '四川省'), (555, '广元市', 1, '四川省'), (556, '遂宁市', 1, '四川省'), (557, '内江市', 1, '四川省'), (558, '乐山市', 1, '四川省'), (559, '南充市', 1, '四川省'), (560, '宜宾市', 1, '四川省'), (561, '广安市', 1, '四川省'), (562, '达州市', 1, '四川省'), (563, '都江堰市', 2, '四川省'), (564, '彭州市', 2, '四川省'), (565, '邛崃市', 2, '四川省'), (566, '崇州市', 2, '四川省'), (567, '广汉市', 2, '四川省'), (568, '什邡市', 2, '四川省'), (569, '绵竹市', 2, '四川省'), (570, '江油市', 2, '四川省'), (571, '峨眉山市', 2, '四川省'), (572, '阆中市', 2, '四川省'), (573, '华蓥市', 2, '四川省'), (574, '万源市', 2, '四川省'), (575, '雅安市', 2, '四川省'), (576, '西昌市', 2, '四川省'), (577, '巴中市', 2, '四川省'), (578, '资阳市', 2, '四川省'), (579, '简阳市', 2, '四川省'), (580, '贵阳市', 1, '贵州省'), (581, '六盘水市', 1, '贵州省'), (582, '遵义市', 1, '贵州省'), (583, '清镇市', 2, '贵州省'), (584, '赤水市', 2, '贵州省'), (585, '仁怀市', 2, '贵州省'), (586, '铜仁市', 2, '贵州省'), (587, '兴义市', 2, '贵州省'), (588, '毕节市', 2, '贵州省'), (589, '安顺市', 2, '贵州省'), (590, '凯里市', 2, '贵州省'), (591, '都匀市', 2, '贵州省'), (592, '福泉市', 2, '贵州省'), (593, '昆明市', 1, '云南省'), (594, '曲靖市', 1, '云南省'), (595, '玉溪市', 1, '云南省'), (596, '安宁市', 2, '云南省'), (597, '宣威市', 2, '云南省'), (598, '昭通市', 2, '云南省'), (599, '楚雄市', 2, '云南省'), (600, '个旧市', 2, '云南省'), (601, '开远市', 2, '云南省'), (602, '思茅市', 2, '云南省'), (603, '景洪市', 2, '云南省'), (604, '大理市', 2, '云南省'), (605, '保山市', 2, '云南省'), (606, '瑞丽市', 2, '云南省'), (607, '潞西市', 2, '云南省'), (608, '西安市', 1, '陕西省'), (609, '铜川市', 1, '陕西省'), (610, '宝鸡市', 1, '陕西省'), (611, '咸阳市', 1, '陕西省'), (612, '渭南市', 1, '陕西省'), (613, '延安市', 1, '陕西省'), (614, '汉中市', 1, '陕西省'), (615, '榆林市', 1, '陕西省'), (616, '兴平市', 2, '陕西省'), (617, '韩城市', 2, '陕西省'), (618, '华阴市', 2, '陕西省'), (619, '安康市', 2, '陕西省'), (620, '商州市', 2, '陕西省'), (621, '兰州市', 1, '甘肃省'), (622, '嘉峪关市', 1, '甘肃省'), (623, '金昌市', 1, '甘肃省'), (624, '白银市', 1, '甘肃省'), (625, '天水市', 1, '甘肃省'), (626, '玉门市', 2, '甘肃省'), (627, '酒泉市', 2, '甘肃省'), (628, '敦煌市', 2, '甘肃省'), (629, '张掖市', 2, '甘肃省'), (630, '武威市', 2, '甘肃省'), (631, '平凉市', 2, '甘肃省'), (632, '西峰市', 2, '甘肃省'), (633, '临夏市', 2, '甘肃省'), (634, '合作市', 2, '甘肃省'), (635, '西宁市', 1, '青海省'), (636, '格尔木市', 2, '青海省'), (637, '德令哈市', 2, '青海省'), (638, '银川市', 1, '宁夏回族自治区'), (639, '石嘴山市', 1, '宁夏回族自治区'), (640, '吴忠市', 1, '宁夏回族自治区'), (641, '青铜峡市', 2, '宁夏回族自治区'), (642, '灵武市', 2, '宁夏回族自治区'), (643, '乌鲁木齐市', 1, '新疆维吾尔族自治区'), (644, '克拉玛依市', 1, '新疆维吾尔族自治区'), (645, '吐鲁番市', 2, '新疆维吾尔族自治区'), (646, '哈密市', 2, '新疆维吾尔族自治区'), (647, '昌吉市', 2, '新疆维吾尔族自治区'), (648, '阜康市', 2, '新疆维吾尔族自治区'), (649, '米泉市', 2, '新疆维吾尔族自治区'), (650, '博乐市', 2, '新疆维吾尔族自治区'), (651, '库尔勒市', 2, '新疆维吾尔族自治区'), (652, '阿克苏市', 2, '新疆维吾尔族自治区'), (653, '阿图什市', 2, '新疆维吾尔族自治区'), (654, '喀什市', 2, '新疆维吾尔族自治区'), (655, '和田市', 2, '新疆维吾尔族自治区'), (656, '奎屯市', 2, '新疆维吾尔族自治区'), (657, '伊宁市', 2, '新疆维吾尔族自治区'), (658, '塔城市', 2, '新疆维吾尔族自治区'), (659, '乌苏市', 2, '新疆维吾尔族自治区'), (660, '阿勒泰市', 2, '新疆维吾尔族自治区'), (134, '拉萨市', 2, '西藏自治区');";
    public final static String NAME_CITY_TABLE = "city";

    /* Table 'category' & 'subcategory' */
    public final static String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE category (amcid INTEGER PRIMARY KEY, category_cn TEXT NOT NULL, category_us TEXT NOT NULL);";
    public final static String SQL_CREATE_SUBCATEGORY_TABLE = "CREATE TABLE if not exists subcategory (ascid INTEGER PRIMARY KEY, subcategory_cn TEXT NOT NULL, subcategory_us TEXT NOT NULL, parent INTEGER NOT NULL);";
    public final static String SQL_INSERT_CATEGORY_TABLE = "INSERT INTO category (amcid, category_cn, category_us) values (1,'运动','Sports'), (2,'学术','Academy'), (3,'休闲','Leisure'), (4,'旅游','Tourism'), (5,'公益','Public Service'), (6,'其他','Others');";
    public final static String SQL_INSERT_SUBCATEGORY_TABLE = "INSERT INTO act_cate_parent (category_cn,category_us, parent) values (1,'足球','Soccer',1), (2,'篮球','Basketball',1), (3,'羽毛球','Badminton',1), (4,'游泳','Swimming',1), (5,'网球','Tennis',1), (6,'保龄球','Bowling',1), (7,'乒乓球','Pingpang',1), (8,'台球','Billiards',1), (9,'瑜伽','Yoga',1), (10,'健身','Fitness',1), (11,'滑雪','Skiing',1), (12,'滑冰','Skating',1), (13,'攀岩','Climbing',1), (14,'极限运动','Extreme Sports',1), (15,'其他','Others',1), (16,'信息技术','Information Technology',2), (17,'艺术','Fine Arts',2), (18,'生命科学','Bio-Science',2), (19,'商业管理','Business',2), (20,'经济金融','Economy&Finance',2), (21,'教育','Education',2), (22,'法律','Law',2), (23,'工程','Engineering',2), (24,'社会','Society',2), (25,'人文','Culture',2), (26,'基础科学','Basic Science',2), (27,'统计','Statistics',2), (28,'其他','Others',2), (29,'桌游','Board Games',3), (30,'KTV','KTV',3), (31,'聚会','Party',3), (32,'演唱会','Concert',3), (33,'棋牌','Chess&Cards',3), (34,'酒吧','Bar',3), (35,'电影','Movies',3), (36,'书友会','Book Club',3), (37,'摄影','Photography',3), (38,'购物','Shopping',3), (39,'烧烤','Barbecue',3), (40,'其他','Others',3), (41,'自然风光','Natural Scenery',4), (42,'城市游','City Tour',4), (43,'古朴小镇','Quaint Town',4), (44,'海外旅游','Overseas Travel',4), (45,'徒步','Hiking',4), (46,'自驾游','Road Trip',4), (47,'露营','Camping',4), (48,'其他','Others',4), (49,'慈善','Charity',5), (50,'志愿者','Volunteer',5), (51,'公益演讲','Charity Speech',5), (52,'其他','Charity Speech',5);";
    public final static String NAME_CATEGORY_TABLE = "category";
    public final static String NAME_SUBCATEGORY_TABLE = "subcategory";

    /* Table 'user' & 'organization' */
    public final static String SQL_INIT_USER_TABLE = "CREATE TABLE if not exists user (uid INTEGER PRIMARY KEY, email TEXT NOT NULL, nickname TEXT NOT NULL, gender INTEGER DEFAULT 1, birthday DATE, city TEXT, country TEXT, lastupdatedtime DATETIME DEFAULE CURRENT_DATETIME, isorganization INTEGER DEFAULT 0);";
    public final static String SQL_INIT_ORGANIZATION_TABLE = "CREATE TABLE if not exists organization (oid INTEGER PRIMARY KEY, organization TEXT NOT NULL, description TEXT NOT NULL, grade INTEGER, isvalid INTEGER DEFAULT 0)";

    /* Table 'activity' */
    public final static String SQL_INIT_ACTIVITY_TABLE = "CREATE TABLE if not exists activity (aid INTEGER PRIMARY KEY, activity TEXT NOT NULL, description TEXT NOT NULL, createtime DATETIME DEFAULT CURRENT_DATETIME, starttime DATETIME NOT NULL, endtime DATETIME NOT NULL, location TEXT NOT NULL, creator TEXT NOT NULL, city TEXT NOT NULL, category TEXT, subcategory TEXT, countlike INTEGER DEFAULT 0, countdislike INTEGER DEFAULT 0, activated INTEGER DEFAULT 0, lati REAL, long REAL);";

    /* Table 'comment' */
    public final static String SQL_INIT_COMMENT_TABLE = "CREATE TABLE if not exists comment(acid INTEGER PRIMARY KEY, aid INTEGER NOT NULL, uid INTEGER NOT NULL, user TEXT NOT NULL, time DATETIME NOT NULL, content TEXT NOT NULL, replytoid INTEGER, replytoname TEXT, readbyreply INTEGER DEFAULT 0, readbyactivityholder INTEGER DEFAULT 0);";
}
