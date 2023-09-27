import { useIsFocused } from "@react-navigation/native";
import React, { useEffect, useState } from "react";
import { View, StyleSheet, TouchableOpacity, Text, ScrollView } from "react-native";
import Feather from "react-native-vector-icons/Feather"; // Feather 아이콘을 가져올 수 있는 라이브러리
import { useDispatch, useSelector } from "react-redux";
import { purposeDetail } from "../../apis/purposeapi"


export default function PurposeDetailScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const isFocused = useIsFocused()
  // const { itemId } = route.params;
  const [purposeSeq, setPurposeSeq] = useState(route.params.purposeSeq)
  const [purposeDetailData, setPurposeDetailData] = useState({})
  // const [purposeDataList, setPurposeDataList] = useState([])


  // 날짜 형식을 변환하는 함수
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${month}월 ${day}일`;
  };

  // const formatTime = (dateString) => {
  //   const date = new Date(dateString);
  //   const hours = date.getHours();
  //   const minutes = date.getMinutes();
  //   return `${hours}:${minutes}`;
  // }
  const formatTime = (dateString) => {
    const date = new Date(dateString);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    
    // 시간과 분을 두 자리 숫자로 표시하도록 수정
    const formattedHours = hours < 10 ? `0${hours}` : hours;
    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
  
    return `${formattedHours}:${formattedMinutes}`;
  }

  // 날짜를 그룹화할 객체 생성
  // const dateGroups = {};
  const [dateGroups, setDateGroups] = useState({});

  // 날짜별로 항목을 그룹화하는 함수
  const groupingDate = (data) => {
    const newDateGroups = { ...dateGroups }; // 이전 상태를 복사합니다.
    data.purposeDataList.forEach((item) => {
      const dateKey = formatDate(item.dataCreatedAt);
      // 새로운 상태를 업데이트할 때는 이전 상태를 변경하지 않고 복사본을 수정합니다.
      if (!newDateGroups[dateKey]) {
        newDateGroups[dateKey] = [];
      }
      newDateGroups[dateKey].push(item);
    });

    // setDateGroups를 사용하여 새로운 상태로 업데이트합니다.
    setDateGroups(newDateGroups);
  };

  const handleData = (data) => {
    setPurposeDetailData(data)
    if(!purposeDetailData.purposeDataList){
      groupingDate(data)
    }
  }


  const doPurposeDetail = async () =>{
    try{
      const response = await purposeDetail(purposeSeq, accessToken, grantType)
      if(response.status===200){
        console.log("목표 상세 조회 성공")
        // setPurposeDetailData(response.data)
        // // setPurposeDataList(response.data.purposeDataList)
        // groupingDate()
        handleData(response.data)
      }else{
        console.log("목표 상세 조회 실패", response.status)
      }
    }catch(error){
      console.log("목표 상세 조회 실패", error)
    }
  }
  

  useEffect(()=>{
    if(isFocused){
      doPurposeDetail()
    }
  }, [isFocused])

  // useEffect(() => {
  //   groupingDate();
  // }, [purposeDetailData]);

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={{ flex: 1, }} />
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Feather name="x" size={30} 
            // style={styles.closeIcon} 
          />
        </TouchableOpacity>
      </View>

      {/* Title Container */}
      <View style={styles.titleContainer}>
        <Text style={styles.purposeTitle}>{purposeDetailData.purposeTitle}</Text>
        <Text style={styles.titleRight}>{purposeDetailData.currentBalance ? purposeDetailData.currentBalance.toLocaleString() : purposeDetailData.currentBalance}원</Text>
      <Text style={styles.goalAmountText}>목표 금액</Text>
      <Text style={styles.goalAmount}>{purposeDetailData.goalAmount ? purposeDetailData.goalAmount.toLocaleString() : purposeDetailData.goalAmount}원</Text>
      </View>

      {/* 구분선 */}
      <View style={styles.divider} />

      {/* Purpose Data 날짜별로 묶기 */}
      <ScrollView 
        style={styles.scrollContainer}
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {Object.entries(dateGroups).map(([date, items]) => (
          <View style={styles.dateGroup} key={date}>
            <Text style={styles.dateText}>{date}</Text>
            {items.map((item) => (
              <View style={styles.dataBox} key={`${item.dataCreatedAt}-${item.dataAmount}-${item.dataCurrentBalance}`}>
                <View style={styles.dataBoxInTop}key={`${item.dataCreatedAt}-${item.dataAmount}-${item.dataCurrentBalance}`}>
                  <Text style={styles.timeText}>{formatTime(item.dataCreatedAt)}</Text>
                  <Text style={styles.dataNameText}>{item.dataName}</Text>
                  <Text style={styles.dataAmountText}>{item.dataAmount ? item.dataAmount.toLocaleString():item.dataAmount}원</Text>
                </View>
                <View style={styles.dataBoxInBottom}>
                  <Text style={styles.dataCurrentBalanceText}>{item.dataCurrentBalance ? item.dataCurrentBalance.toLocaleString() : item.dataCurrentBalance}원</Text>
                </View>
              </View>
            ))}
          </View>
        ))}

        <View style={styles.purposeStopContainer}>
          <TouchableOpacity
            style={styles.stopPurposeButton}
            onPress={() => navigation.navigate("PurposeStopScreen", {purposeSeq:purposeSeq, purposeDetailData:purposeDetailData})}
          >
            <Text style={styles.stopPurposeText}>그래프로 보기</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    width: "90%",
    justifyContent: "flex-end",
    paddingVertical: 10,
    marginTop: 20,
    marginBottom: 20,
  },
  titleContainer: {
    width: "90%",
    alignItems: "left",
    marginBottom: 20,
  },
  purposeTitle: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 20,
  },
  titleRight: {
    fontSize: 30,
    fontWeight: "bold",
    color: "#FF965C",
    alignSelf: "flex-end",
  },
  goalAmountText: {
    fontSize: 15,
    marginBottom: 5,
  },
  goalAmount: {
    fontSize: 15,
    fontWeight: "bold",
  },
  divider: {
    width: "100%",
    height: 2,
    backgroundColor: "#D0D4DA",
    marginBottom: 10,
  },
  scrollContainer: {
    marginTop: 20,
    width: "100%",
  },
  scrollContent: {
    flexGrow: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  dateContainer: {
    flexDirection: "column",
    alignItems: "flex-start",
    marginBottom: 20,
  },
  dateText: {
    fontSize: 15,
    fontWeight: "bold",
    marginBottom: 10,
    marginTop: 20,
  },
  dataBox: {
    width: "100%",
    height: 80,
    justifyContent: "space-between",
    borderWidth: 1,
    borderColor: "#D0D4DA",
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
  },
  dataBoxInTop: {
    width: "90%",
    flexDirection: "row",
    justifyContent: "space-between",
  },
  timeText: {
    alignSelf: "center",
    marginRight: 13,
  },
  dataNameText: {
    fontSize: 15,
    flex: 1,
    alignSelf: "center",
  },
  dataAmountText: {
    fontSize: 15,
    alignSelf: "center",
  },
  dataBoxInBottom: {
    fontSize: 15,
    width: "90%",
    flexDirection: "row",
    alignSelf: "flex-end",
    marginBottom: 10,
  },
  dataCurrentBalanceText: {
    fontSize: 13,
    alignSelf: "flex-end",
    color: "#858585",
  },
  closeIcon: {
    marginRight: 20,
  },
  purposeStopContainer: {
    width: "90%",
    alignItems: "flex-end",
    justifyContent: "center",
    margin: 20,
  },
  stopPurposeButton: {
    alignItems: "center",
    justifyContent: "center",
    marginRight: 10,
  },
  stopPurposeText: {
    fontSize: 13,
    color: "#939393",
  },
});
