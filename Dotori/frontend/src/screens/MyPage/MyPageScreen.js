import React, { useEffect, useState } from "react";
import AsyncStorage from '@react-native-async-storage/async-storage'
import { View, Text, StyleSheet, TouchableOpacity, Image, Modal, Alert, Pressable } from "react-native";
import { AntDesign, Entypo, FontAwesome } from "@expo/vector-icons";

// const userInfo = {
//   name: "홍길동",
//   email: "example@example.com",
//   birth: "2000-01-01",
//   phone: "010-1111-1111",
// };

import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import {userLogout,userWithdrawDotori, userInfoInquiry} from "../../apis/userapi"
import {inputgrantType, inputAccessToken, inputRefreshToken} from "../../redux/slices/auth/user"
import { useIsFocused } from "@react-navigation/native";

export default function MyPageScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const [userInfo, setUserInfo] = useState(null)
  const isFocused = useIsFocused();
  const [userWithdrawModalVisible, setUserWithdrawModalVisible] = useState(false);


  const doLogout = async () =>{
    try{
      const response = await userLogout(refreshToken, accessToken, grantType)
      if(response.status === 200){
        console.log("로그아웃 성공")
        try {
          dispatch(inputgrantType(null))
          dispatch(inputAccessToken(null))
          dispatch(inputRefreshToken(null))
          navigation.reset({
            index: 0,
            routes: [{ name: 'LoginScreen' }],
          });
        } catch(error) {
          console.log(error)
        }
      }else if(response.status===403){
        console.log("로그아웃 실패 토큰없음", response.status)
      }else{
        console.log("로그아웃 실패", response.status)
      }
    }catch(error){
      console.log("오류발생: 로그아웃 실패", error)
    }
  }

  const handleLogout = () => {
    doLogout()
  }

  const hanldeuserWithdrawDotori = async () =>{
    setUserWithdrawModalVisible(true)
  }

  const changeFormPhoneNumber = (phoneNumber) => {
    const firstNumber = phoneNumber.slice(0,3)
    const middleNumber = phoneNumber.slice(3,7)
    const lastNumber = phoneNumber.slice(7)
    return `${firstNumber}-${middleNumber}-${lastNumber}`
  }

  const doUserInfoInquiry = async () => {
    try {
      const response = await userInfoInquiry(accessToken, grantType);
      if(response.status===200){
        setUserInfo(response.data);
      }else{
        console.log('사용자 정보 조회 실패',response.status)
      }
    } catch (error) {
      console.error('오류 발생 : 사용자 정보 조회 실패:', error);
    }
  }

  useEffect(() => {
    if(isFocused){
      doUserInfoInquiry();
    }
  }, [isFocused]);


  return (
    <View style={styles.container}>
      <HeaderComponent title="마이페이지" navigation={navigation} cancelNavi={"MainPageScreen"}></HeaderComponent>

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
          {/* <Text style={styles.infoText}>{userInfo.name}</Text> */}
          <Text style={styles.infoText}>{userInfo?userInfo.userName:'박새로이'}</Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>이메일</Text>
          {/* <Text style={styles.infoText}>{userInfo.email}</Text> */}
          <Text style={styles.infoText}>{userInfo?userInfo.id:"dotori123@dotori.com"}</Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>생년월일</Text>
          {/* <Text style={styles.infoText}>{userInfo.birth}</Text> */}
          <View style={styles.infoRow}>
            <Text style={styles.infoText}>{userInfo?userInfo.birthDate:"1999-12-12"} </Text>
            <TouchableOpacity
              onPress={() => navigation.navigate("EditBirthDateScreen", {birthDate: userInfo.birthDate})}
            >
              <Image
                style={styles.editIcon}
                source={require("../../assets/icon/pencil.png")} // 편집 아이콘 이미지 경로
              />
            </TouchableOpacity>
          </View>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoText}>핸드폰 번호</Text>
          <View style={styles.infoRow}>
            {/* <Text style={styles.infoText}>{userInfo.phone}</Text> */}
            <Text style={styles.infoText}>{userInfo?changeFormPhoneNumber(userInfo.phoneNumber):"010-1234-1234"} </Text>
            {/* <Text style={styles.infoText}>{userInfo?userInfo.phoneNumber:"010-1234-1234"}</Text> */}
            <TouchableOpacity
              onPress={() => navigation.navigate("EditPhoneNumberScreen", {phoneNumber: userInfo.phoneNumber})}
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
          onPress={() => {
            navigation.navigate("PasswordChangeScreen")
          }}
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
        <TouchableOpacity 
          style={styles.logoutButton}
          onPress={()=>{
            handleLogout()
          }}
        >
          <Text style={styles.logoutText}>로그아웃</Text>
        </TouchableOpacity>
        <Text style={styles.separatorText}> </Text>
        <TouchableOpacity style={styles.withdrawButton}
          onPress={()=>{
            hanldeuserWithdrawDotori()
          }}
        >
          <Text style={styles.withdrawText}>회원탈퇴</Text>
        </TouchableOpacity>
      </View>
      
      {/* 회원탈퇴 모달창 */}
      <View style={styles.centeredView}>
        <Modal
          animationType="none"
          transparent={true}
          visible={userWithdrawModalVisible}
          onRequestClose={() => {
            // Alert.alert('Modal has been closed.');
            setUserWithdrawModalVisible(false);
          }}>
          <View style={styles.centeredView}>
            <View style={styles.modalView}>
              <Text style={styles.modalText}>회원탈퇴를 진행 하시겠습니까?</Text>
              <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => {
                    setUserWithdrawModalVisible(false)
                    navigation.navigate("WithDraw1Screen", {userInfo : userInfo})
                    // navigation.reset({
                    //   index: 0,
                    //   routes: [{ name: 'MainPageScreen' }],
                    // });
                  }}>
                  <Text style={styles.textStyle}>예</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => {
                    setUserWithdrawModalVisible(false)
                  }}>
                  <Text style={styles.textStyle}>아니오</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
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
  // 모달 관련 스타일
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    // alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  button: {
    flex:1,
    borderRadius: 20,
    padding: 10,
    marginHorizontal: 10,
    elevation: 2,
  },
  buttonOpen: {
    backgroundColor: '#F194FF',
  },
  buttonClose: {
    // backgroundColor: '#2196F3',
    backgroundColor: '#FF965C',
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
  },
});
