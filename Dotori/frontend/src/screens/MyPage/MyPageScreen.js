import React from "react";
import { View, Text, StyleSheet, TouchableOpacity, Image } from "react-native";
import { AntDesign, Entypo, FontAwesome } from "@expo/vector-icons";

const userInfo = {
  name: "홍길동",
  email: "example@example.com",
  birth: "2000-01-01",
  phone: "010-1111-1111",
};
import HeaderComponent from "../Components/HeaderScreen";

export default function MyPageScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <HeaderComponent title="마이페이지"></HeaderComponent>

      {/* 프로필 이미지 */}
      <View style={styles.profileContainer}>
        <Image
          style={styles.profileImage}
          source={require("../../assets/images/Hamster/MainHamster.png")} // 프로필 이미지 경로
        />
      </View>

      {/* 구분선 */}
      <View style={styles.separator}></View>

      {/* 기본 정보 */}
      <View style={styles.basicContainer}>
        <Text style={styles.boldText}>기본 정보</Text>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>이름</Text>
          <Text style={styles.infoText}>{userInfo.name}</Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>이메일</Text>
          <Text style={styles.infoText}>{userInfo.email}</Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>생년월일</Text>
          <Text style={styles.infoText}>{userInfo.birth}</Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>핸드폰 번호</Text>
          <View style={styles.infoRow}>
            <Text style={styles.infoText}>{userInfo.phone}</Text>
            <TouchableOpacity
              onPress={() => navigation.navigate("EditPhoneNumberScreen")}
            >
              <Image
                style={styles.editIcon}
                source={require("../../assets/icon/pencil.png")} // 편집 아이콘 이미지 경로
              />
            </TouchableOpacity>
          </View>
        </View>
      </View>

      {/* 구분선 */}
      <View style={styles.separator}></View>

      {/* 기능 항목 */}
      {/* <Text style={styles.boldText}>기능</Text> */}
      <View style={styles.basicContainer}>
        <TouchableOpacity
          style={styles.featureItem}
          onPress={() => navigation.navigate("PasswordChangeScreen")}
        >
          <Text style={styles.infoText}>비밀번호 변경</Text>
          <FontAwesome
            name="angle-right"
            style={{ fontSize: 30 }}
          ></FontAwesome>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.featureItem}
          onPress={() => navigation.navigate("CategoryScreen")}
        >
          <Text style={styles.infoText}>카테고리 보기</Text>
          <FontAwesome
            name="angle-right"
            style={{ fontSize: 30 }}
          ></FontAwesome>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.featureItem}
          onPress={() => navigation.navigate("ReceipeSelectScreen")}
        >
          <Text style={styles.infoText}>명세서 보기</Text>
          <FontAwesome
            name="angle-right"
            style={{ fontSize: 30 }}
          ></FontAwesome>
        </TouchableOpacity>
      </View>

      {/* 로그아웃과 회원탈퇴 */}
      <View style={styles.bottomRow}>
        <TouchableOpacity style={styles.logoutButton}>
          <Text style={styles.logoutText}>로그아웃</Text>
        </TouchableOpacity>
        <Text style={styles.separatorText}> </Text>
        <TouchableOpacity style={styles.withdrawButton}>
          <Text style={styles.withdrawText}>회원탈퇴</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
  },
  basicContainer: {
    paddingHorizontal: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 15,
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
  myPageText: {
    fontSize: 24,
    flex: 1, // 텍스트가 남은 공간을 모두 차지하도록 설정
    textAlign: "center",
  },
  profileContainer: {
    alignItems: "center",
  },
  profileImage: {
    width: 100,
    height: 100,
    borderRadius: 50, // 원형 모양으로 만들기 위한 반지름 설정
    marginVertical: 16,
  },
  separator: {
    height: 5,
    backgroundColor: "#F2EFEF",
    marginVertical: 16,
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 20,

    marginBottom: 16,
  },
  infoItem: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 8,
    marginTop: 8,
  },
  infoText: {
    fontSize: 15,
  },
  infoRow: {
    flexDirection: "row",
    alignItems: "center",
  },
  editIcon: {
    width: 15,
    height: 15,
  },
  featureItem: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 10,
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
  bottomRow: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    marginTop: 35,
  },
  logoutButton: {
    borderBottomWidth: 1,
    borderBottomColor: "#3D3D3D",
    paddingBottom: 2,
  },
  logoutText: {
    fontSize: 10,
  },
  separatorText: {
    marginHorizontal: 20,
  },
  withdrawButton: {
    borderBottomWidth: 1,
    borderBottomColor: "#FF3737",
    paddingBottom: 2,
  },
  withdrawText: {
    fontSize: 10,
    color: "#FF3737",
  },
});
