import React, {useState, useEffect} from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import HeaderComponent from "../Header/HeaderScreen";
import FooterScreen from "../Header/FooterScreen";
import {
  userInformationInquiry, userLogout
} from '../../apis/userapi'
import { FontAwesome } from "@expo/vector-icons";

export default function MyPageScreen({ navigation }) {
  const [userInfo, setUserInfo] = useState(null)

  //들어올 때 axios로 정보 받아야함
  const hanldeUserInformationInquiry = async() =>{
    const response = await userInformationInquiry()
    if(response.status===200){
      console.log('good')
      setUserInfo(response.data)
    }else if(response.status===400){
      console.log('bad400 회원정보를 받아올 수 없습니다.')
    }else if(response.status===401){
      console.log('bad401 회원정보를 받아올 수 없습니다.')
    }
  }

  const handleUserLogout = () => {
    const response = userLogout()
    if(response===200){
      console.log('logout 성공')
      navigation.navigate("LoginScreen");
    }else if(response===400){
      console.log('logout 실패')
    }else if(response===401){
      console.log('logout 인증실패 실패')
    }else if(response===403){
      console.log('토큰없음')
    }else{
      console.log('오류발생 로그아웃 실패')
    }
  }


  // 페이지가 로드될 때 hanldeUserInformationInquiry() 함수 실행
  useEffect(() => {
    hanldeUserInformationInquiry();
  }, []);



  return (
    <View style={styles.container}>
      {/* Header Component (You can replace this with your own component) */}
      <HeaderComponent
        title="마이페이지"
        navigation={navigation}
        showHome={false}
      />

      {/* User Information */}
      <View style={styles.userInfoContainer}>
        <Text style={styles.boldText}>기본정보</Text>
        <View style={styles.infoRow}>
          <Text>이름</Text>
          {/* <Text>정예진 {userInfo.name}</Text> */}
          <Text>정예진</Text>
        </View>
        <View style={styles.infoRow}>
          <Text>이메일</Text>
          {/* <Text>abc1234@naver.com {userInfo.email}</Text> */}
          <Text>abc1234@naver.com</Text>
        </View>
        <View style={styles.infoRow}>
          <Text>생년월일</Text>
          {/* <Text>1999.05.08 {userInfo.birthdate}</Text> */}
          <Text>1999.05.08</Text>
        </View>
        <View style={styles.infoRow}>
          <Text>핸드폰번호</Text>
          {/* <Text>010-****-1908 {userInfo.phoneNumber}</Text> */}
          <Text>010-****-1908</Text>
        </View>
      </View>

      <View style={styles.divider} />

      {/* Password Change and Logout */}
      <TouchableOpacity
        style={styles.actionRow}
        onPress={()=>{
          navigation.navigate('ChangePasswordScreen')
        }}
      >
        <Text>비밀번호 변경</Text>
        <FontAwesome name="angle-right" style={{ fontSize: 20 }}></FontAwesome>
      </TouchableOpacity>
      <TouchableOpacity
        //accessToken지우자
        style={styles.logoutButton}
        onPress={() => {
          // handleUserLogout()
          navigation.navigate("LoginScreen");
        }}
      >
        <Text style={styles.logoutButtonText}>로그아웃</Text>
      </TouchableOpacity>
      <TouchableOpacity>
        <Text style={styles.grayText}>
          포도은행을 탈퇴하시려면 여기를 눌러주세요
        </Text>
      </TouchableOpacity>

      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 20,
  },

  boldText: {
    fontWeight: "bold",
    marginBottom: 30,
    fontSize: 25,
  },
  userInfoContainer: {
    marginBottom: 20,
    marginTop: 50,
  },
  infoRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  divider: {
    height: 5,
    backgroundColor: "#D9D9D9",
    marginBottom: 20,
    marginLeft: -20,
    marginRight: -20,
  },
  actionRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  logoutButton: {
    width: "100%",
    padding: 15,
    backgroundColor: "#842DC480",
    marginBottom: 20,
    alignItems: "center",
    borderRadius: 8,
    marginTop: 40,
  },
  logoutButtonText: {
    color: "white",
    fontSize: 16,
  },
  grayText: {
    color: "gray",
    textDecorationLine: "underline",
    marginBottom: 20,
  },
  footer: {
    justifyContent: "flex-start",
    alignItems: "center",
    marginTop: 220,
  },
});
