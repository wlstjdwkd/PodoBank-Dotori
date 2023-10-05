import React, { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  FlatList,
  Dimensions,
  ScrollView,
  PanResponder,
  Alert,
  Modal,
  Button,
  Animated,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { planCategoryUsingSpot, planCategoryDeleteSpot } from "../../apis/planapi"

export default function CategorySettingScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const [categorySeq, setCategorySeq] = useState(route.params.categorySeq);
  const [categoryTitle, setCategoryTitle] = useState(route.params.categoryTitle)
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [selectedSpot, setSelectedSpot] = useState(null)


  
  // categorySeq와 일치하는 category 데이터를 찾습니다.
  const [categorySpot, setCategorySpot] = useState(null)

  // 카테고리 사용처 목록 가져오기 함수
  const doPlanCategoryUsingSpot = async () => {
    try{
      const response = await planCategoryUsingSpot(categorySeq, accessToken, grantType)
      if(response.status===200){
        console.log("카테고리 사용처 목록 데이터:",response.data)
        setCategorySpot(response.data)
        console.log('카테고리 사용처 목록 가져오기 성공') 
      }else{
        console.log('카테고리 사용처 목록 가져오기 실패',response.status) 
      } 
    }catch(error){
      console.log('카테고리 사용처 목록 가져오기 실패',error) 
    }
  }

  const doPlanCategoryDeleteSpot = async (dataCode) => {
    try{
      const response = await planCategoryDeleteSpot(dataCode, accessToken, grantType)
      if(response.status===200){
        // setCategorySpot(response.data)
        console.log('카테고리 사용처 삭제하기 성공') 
      }else{
        console.log('카테고리 사용처 삭제하기 실패',response.status) 
      } 
    }catch(error){
      console.log('카테고리 사용처 삭제하기 실패',error) 
    }
  }


  

  useEffect(()=>{
    doPlanCategoryUsingSpot()
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent title="카테고리 세부 내용" navigation={navigation} cancelNavi="MyPageScreen"></HeaderComponent>

      <View style={styles.topContainer}>
        {/* 페이지 상단 좌측에 categoryTitle 출력 */}
        <View style={styles.categoryTitleContainer}>
          <Text style={styles.categoryTitle}>{categoryTitle}</Text>
          <Text style={styles.categoryTitleExplain}>
            자주 사용하는 사용처를 분류해주세요.
          </Text>
        </View>
        <Image
          style={styles.checkIcon}
          source={require("../../assets/images/Hamster/CheckHamster.png")}
        />
      </View>

      {/* 자주 사용하는 사용처 */}
      <View style={styles.categoryGroup}>
        <Text style={styles.categoryGroupText}>자주 사용하는 사용처</Text>
      </View>
      {/* 카테고리 목록 */}
      {categorySpot
        ?(<View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
          {categorySpot.map((item, index) => {
            if(item.count > 1){
              return(
                <TouchableOpacity
                  key={index}
                  style={[
                    styles.categoryItem,
                  ]}
                  onPress={()=>{
                    setIsDeleteModalVisible(true)
                    setSelectedSpot({dataCode:item.dataCode, dataName:item.dataName})
                  }}
                >
                  <Text style={styles.categoryItemText}>{item.dataName}</Text>
                </TouchableOpacity>
              )
            }else{
              return null
            }
          })}
        </View>)
        : (<View style={{ flexDirection: 'row', flexWrap: 'wrap' }}></View>)
      }
      {/* 구분선 */}
      <View style={styles.separator}></View>
      {/* 신규 사용처 */}
      <View style={styles.categoryGroup}>
        <Text style={styles.categoryGroupText}>신규 사용처</Text>
      </View>
      {/* 신규 카테고리 목록 */}
      {categorySpot
        ?(<View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
          {categorySpot.map((item, index) => {
            if(item.count ==1){
              return(
                <TouchableOpacity
                  key={index}
                  style={[
                    styles.categoryItem,
                  ]}
                  onPress={()=>{
                    setIsDeleteModalVisible(true)
                    setSelectedSpot({dataCode:item.dataCode, dataName:item.dataName})
                  }}
                >
                  <Text style={styles.categoryItemText}>{item.dataName}</Text>
                </TouchableOpacity>
              )
            }else{
              return null
            }
          })}
        </View>)
        : (<View style={{ flexDirection: 'row', flexWrap: 'wrap' }}></View>)
      }

      {/* 카테고리 사용처 삭제 모달 */}
      {isDeleteModalVisible
        ?(<View style={styles.centeredView}>
          <Modal
            animationType="none"
            transparent={true}
            visible={isDeleteModalVisible}
            onRequestClose={() => {
              setIsDeleteModalVisible(false)
              setSelectedSpot(null)
            }}>
            <View style={styles.centeredView}>
              <View style={styles.modalView}>
                <Text style={styles.modalText}>{selectedSpot.dataName}를 {categoryTitle} 카테고리에서 삭제 하시겠습니까?</Text>
                <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      doPlanCategoryDeleteSpot(selectedSpot.dataCode)
                      const newCategorySpot = categorySpot.filter(item => item.dataCode !== selectedSpot.dataCode);
                      setCategorySpot(newCategorySpot)
                      setIsDeleteModalVisible(false)
                      setSelectedSpot(null)
                    }}>
                    <Text style={styles.textStyle}>예</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      setIsDeleteModalVisible(false)
                      setSelectedSpot(null)
                    }}>
                    <Text style={styles.textStyle}>아니오</Text>
                  </TouchableOpacity>
                </View>
              </View>
            </View>
          </Modal>
        </View>)
        :null
      }
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
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
  topContainer: {
    flexDirection: "row",
    // alignContent: "place-between",
    marginLeft: "5%",
    // width: "90%",
    marginTop: 20,
    marginBottom: 50,
  },
  categoryTitleContainer: {
    flexDirection: "column",
  },
  categoryTitle: {
    fontSize: 20,
    fontWeight: "bold",

    flex: 1,
    textAlign: "left",
    marginLeft: 8, // categoryTitle을 뒤에 추가
    marginTop: 70,
    marginBottom: -10,
  },
  categoryTitleExplain: {
    fontSize: 10,
    flex: 1,
    textAlign: "left",
    marginLeft: 8, // categoryTitle을 뒤에 추가
    color: "#7B7B7B",
  },
  categoryGroup: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 20,
  },
  categoryGroupText: {
    fontSize: 20,
    fontWeight: "bold",
    marginLeft: "5%",
    marginBottom: 10,
  },
  checkIcon: {
    width: 150,
    height: 150,
    marginLeft: 10,
  },
  categoryList: {
    flexDirection: "column", // 세로로 배치
    alignSelf: "left",
  },
  categoryItem: {
    alignItems: "center",
    padding: 4,
    marginBottom: 8,
    marginHorizontal: 8, // 좌우 여백 추가
    borderWidth: 1, // 테두리 추가
    borderColor: "#7B7B7B", // 테두리 색상
    borderRadius: 20, // 테두리 둥글게 설정
  },
  categoryItemText: {
    fontSize: 16,
    marginHorizontal: 5,
  },
  separator: {
    height: 1,
    backgroundColor: "#BAC0CA", // 구분선 색상 변경
    marginVertical: 40,
  },
  circle: {
    width: 60, // 원의 너비
    height: 60, // 원의 높이
    borderRadius: 50, // 반지름의 절반 크기로 설정하여 원 모양으로 만듭니다.
    borderWidth: 1, // 테두리 두께
    borderColor: "#7B7B7B", // 테두리 색상
    backgroundColor: "#7B7B7B",
    padding: 0, // 원 내부에 패딩 적용
    justifyContent: 'center', // 원 내부 컨텐츠를 가운데 정렬
    alignItems: 'center', // 원 내부 컨텐츠를 가운데 정렬
    bottom: 50
  },
  circle2: {
    width: 80, // 원의 너비
    height: 80, // 원의 높이
    borderRadius: 50, // 반지름의 절반 크기로 설정하여 원 모양으로 만듭니다.
    borderWidth: 1, // 테두리 두께
    borderColor: "black", // 테두리 색상
    backgroundColor: "black",
    padding: 0, // 원 내부에 패딩 적용
    justifyContent: 'center', // 원 내부 컨텐츠를 가운데 정렬
    alignItems: 'center', // 원 내부 컨텐츠를 가운데 정렬
    bottom: 40
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
