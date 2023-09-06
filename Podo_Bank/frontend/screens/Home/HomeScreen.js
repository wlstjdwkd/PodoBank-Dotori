import React from "react";
import {
  View,
  Text,
  Image,
  Button,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { AntDesign, MaterialCommunityIcons } from "@expo/vector-icons"; // AntDesign과 Entypo 아이콘 라이브러리 추가
import BottomBarScreen from "../Header/BottomBarScreen";

export default function HomeScreen({ navigation }) {
  //대표계좌 axios로 가져와야함

  return (
    <View style={styles.container}>
      {/* 사용자 이름 */}
      <View style={styles.nameContainer}>
        <Text style={styles.userNameBold}>정예진</Text>
        <Text style={styles.userName}>님</Text>
      </View>

      {/* 대표 계좌 */}
      <View style={styles.accountBox}>
        <View style={styles.bankInfo}>
          <Image
            source={require("../../assets/images/normal_podo.png")}
            style={styles.bankLogo}
          />
          <Text style={styles.bankName}>포도은행 통장</Text>
        </View>

        <Text style={styles.accountNumber}>1235-4568-4532</Text>
        <Text style={styles.balance}>1,000,000,000원</Text>
        <TouchableOpacity
          style={styles.transferButton}
          onPress={() => alert("이체 화면으로 이동합니다.")}
        >
          <Text style={styles.buttonText}>이체</Text>
        </TouchableOpacity>
      </View>

      {/* 계좌 추가 버튼 */}
      <View style={styles.addAccountBox}>
        <AntDesign name="pluscircleo" size={25} color="black" />
      </View>

      {/* 설정 버튼 */}
      <View style={styles.settingsContainer}>
        <MaterialCommunityIcons
          name="cog-outline"
          size={20}
          style={styles.settingsIcon}
        />
        <Text style={styles.settingsText}>대표계좌 설정</Text>
      </View>
      <BottomBarScreen navigation={navigation} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "flex-start",
    paddingHorizontal: 20,
    paddingTop: 100,
    backgroundColor: "white",
  },
  accountNumber: {
    fontSize: 15,
    marginLeft: 25,
    marginBottom: 10,
    alignSelf: "flex-start",
  },
  balance: {
    fontSize: 25,
    marginTop: 20,
    marginBottom: 30,
    marginLeft: 25,
    alignSelf: "flex-start",
  },
  buttonText: {
    color: "black",
    fontSize: 16,
    fontWeight: "bold",
  },
  nameContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    width: "100%",
    marginBottom: 20,
  },
  userNameBold: {
    fontSize: 24,
    fontWeight: "bold",
  },
  userName: {
    fontSize: 24,
  },
  accountBox: {
    width: "100%",
    borderRadius: 10,
    padding: 20,
    backgroundColor: "white",
    marginBottom: 20,
    borderColor: "#757575",
    borderWidth: 1,
  },
  bankInfo: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 10,
  },
  bankLogo: {
    width: 25,
    height: 25,
  },
  bankName: {
    fontSize: 20,
    fontWeight: "bold",
  },
  transferButton: {
    marginTop: 20,
    backgroundColor: "#8B0FD790",
    padding: 10,
    borderRadius: 3,
    alignItems: "center",
    marginLeft: 25,
    marginRight: 25,
  },
  addAccountBox: {
    width: "100%",
    height: 60,
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 20,
    borderRadius: 10,
    borderColor: "#757575",
    borderWidth: 1,
  },
  settingsContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
  },
  settingsIcon: {
    marginRight: 10,
  },
  settingsText: {
    fontSize: 16,
  },
});
