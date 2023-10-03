import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Image, Alert } from "react-native";
import { Picker } from "@react-native-picker/picker";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { planSpecificationList } from "../../apis/planapi"

const specifications = [
  {
    accountName: "힘들어",
    planSeq: 0,
    startAt: "2023-07-15 09:10",
    endAt: "2023-07-25 09:09" ,
  },
  {
    accountName: "너무",
    planSeq: 1,
    startAt: "2023-08-01 15:20",
    endAt: "2023-08-10 15:19",
  },
  {
    accountName: "힘들어",
    planSeq: 2,
    startAt: "2023-08-01 20:12",
    endAt: "2023-08-10 20:11",
  },
  {
    accountName: "프론트",
    planSeq: 3,
    startAt: "2023-09-01 18:59",
    endAt: "2023-09-10 18:58",
  },
];

export default function ReceipeSelectScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  
  const [specificationList, setSpecificationList] = useState([])
  // const [specificationList, setSpecificationList] = useState(specifications)
  const [selectedAccount, setSelectedAccount] = useState(null)
  const [selectedReceipe, setSelectedReceipe] = useState(null)

  const handleViewReceipe = () => {
    // 명세서 보기 화면으로 이동하면서 선택한 계좌와 명세서 번호(receipeSeq)를 전달
    if(selectedAccount && (selectedReceipe != null)){
      // 나중에 원상복귀 시켜야함.
      navigation.navigate("ReceipeScreen", {
        selectedAccount,
        selectedReceipe,
      });
      // 잠깐 쓰고 나중에 삭제할네비게이션임.
      // navigation.navigate("SavingPlanCompleteRecipeScreen", {selectedAccount:selectedAccount, selectedReceipe:selectedReceipe, })
    }else{
      Alert.alert('','확인할 수 있는 명세서가 없습니다.')
    }
  };

  const changeDayForm = (changingDay) =>{
    const year = changingDay.slice(0,4)
    const month = changingDay.slice(5,7)
    const day = changingDay.slice(8,10)
    return `${year}.${month}.${day}`
  }

  // 명세서 전체 리스트 받아오기
  const doPlanSpecificationList = async () =>{
    try{
      const response = await planSpecificationList(accessToken, grantType)
      if(response.status === 200){
        setSpecificationList(response.data)
        if(response.data.length > 0){
          setSelectedAccount(response.data[0].accountName)
          setSelectedReceipe(response.data[0].planSeq)
        }
        console.log("명세서 전체 리스트 받아오기 성공")
      }else{
        console.log("명세서 전체 리스트 받아오기 실패")
      }
    }catch(error){
      console.log("오류발생 명세서 전체 리스트 받아오기 실패:", error)
    }
  }

  useEffect(()=>{
    doPlanSpecificationList()
    // if(specificationList.length>0){
    //   setSelectedAccount(specificationList[0].accountName)
    //   setSelectedReceipe(specificationList[0].planSeq)
    // }
  }, [])

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="명세서 보기"
        navigation={navigation}
      ></HeaderComponent>

      <View style={styles.container}>
        {/* 명세서 선택 텍스트 */}
        <Text style={styles.descriptionText}>
          명세서의 기간을 선택해 주세요
        </Text>


        {specificationList.length>0
          ?
            (
            <View style={styles.pickerContainer}>
              <View style={styles.pickerItem}>
                <View style={{ alignItems: "center", borderBottomWidth: 1 }}>
                  <Text style={styles.dropdownLabel}>계좌 이름</Text>
                </View>
                <Picker
                  style={styles.dropdownPicker}
                  selectedValue={selectedAccount}
                  onValueChange={(itemValue) => setSelectedAccount(itemValue)}
                  itemStyle={{ textAlign: "center" }} // 이 줄 추가
                >
                  {specifications
                    .filter((receipe, index, self) =>
                      index === self.findIndex((r) => r.accountName === receipe.accountName)
                    )
                    .map((uniqueReceipe, index) => (
                      <Picker.Item
                        key={index}
                        label={uniqueReceipe.accountName}
                        value={uniqueReceipe.accountName}
                      />
                    ))}
                </Picker>
              </View>
              <View style={styles.pickerItem}>
                <View style={{ alignItems: "center", borderBottomWidth: 1 }}>
                  <Text style={styles.dropdownLabel}>기간</Text>
                </View>
                <Picker
                  style={styles.dropdownPicker}
                  selectedValue={selectedReceipe}
                  onValueChange={(itemValue, itemIndex) => {
                    setSelectedReceipe(itemValue);
                  }}
                  itemStyle={{ textAlign: "center" }} // 이 줄 추가
                >
                  {specifications.map((receipe, index) => {
                    if(receipe.accountName === selectedAccount){
                      return (
                        <Picker.Item
                          key={index}
                          label={`${changeDayForm(receipe.startAt)} ~ ${changeDayForm(receipe.endAt)}`}
                          value={receipe.planSeq}
                        />
                      )
                    }
                  })}
                </Picker>
              </View>
            </View>)
          : 
            (
              <View style={styles.pickerContainer}>
                <View style={styles.pickerItem}>
                  <View style={{ alignItems: "center", borderBottomWidth: 1 }}>
                    <Text style={styles.dropdownLabel}>계좌 이름</Text>
                  </View>
                  <Picker
                    style={styles.dropdownPicker}
                    selectedValue={selectedAccount}
                    onValueChange={()=>{}}
                    itemStyle={{ textAlign: "center" }} // 이 줄 추가
                  >
                        <Picker.Item
                          label="해당없음"
                          value=""
                        />
                  </Picker>
                </View>
                <View style={styles.pickerItem}>
                  <View style={{ alignItems: "center", borderBottomWidth: 1 }}>
                    <Text style={styles.dropdownLabel}>기간</Text>
                  </View>
                  <Picker
                    style={styles.dropdownPicker}
                    selectedValue={selectedReceipe}
                    onValueChange={()=>{}}
                    itemStyle={{ textAlign: "center" }} // 이 줄 추가
                  >
                    
                    <Picker.Item
                      label="해당없음"
                      value=""
                    />
                  </Picker>
                </View>
              </View>)
        }
        {/* 명세서 보기 버튼 */}
        <TouchableOpacity
          style={styles.viewReceipeButton}
          onPress={handleViewReceipe}
        >
          <Text style={styles.viewReceipeButtonText}>명세서 보기</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
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
  selectReceipeText: {
    fontSize: 24,
    flex: 1,
    textAlign: "center",
    fontWeight: "bold", // 텍스트를 굵게 설정
  },
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
    justifyContent: "center", // 수직 중앙 정렬
  },
  descriptionText: {
    fontSize: 20,
    textAlign: "left",
    fontWeight: "bold", // 텍스트를 굵게 설정
    marginTop: -100,
    marginBottom: 60,
    marginLeft: "5%",
  },
  pickerContainer: {
    flexDirection: "row", // 가로로 배치
    justifyContent: "space-between", // 간격을 균등하게 분배
    marginBottom: 20,
    marginHorizontal: "5%",
  },
  pickerItem: {
    flex: 1, // 1:1 비율로 공간을 분배
    marginRight: 8, // 간격 설정
    borderWidth: 1,
    borderRadius: 10,
  },
  dropdownLabel: {
    fontSize: 16,
    // fontWeight: "bold", // 라벨 텍스트 굵게 설정
    // borderWidth: 1,
    // alignContent: "center",
    // borderBottomWidth: 1,
  },
  dropdownPicker: {
    height: 40,
    borderWidth: 1,
    borderColor: "#7B7B7B", // 테두리 색상 설정
    borderRadius: 8,
  },
  viewReceipeButton: {
    width: "100%",
    backgroundColor: "#FF965C",
    borderRadius: 8,
    padding: 8,
    alignSelf: "center",
    alignItems: "center",
    marginVertical: 20,
  },
  viewReceipeButtonText: {
    color: "white", // 흰색 텍스트
    fontWeight: "bold", // 텍스트 굵게 설정
    fontSize: 16,
  },
});
