import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  TouchableWithoutFeedback,
  Modal,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";
import { Calendar, LocaleConfig, markedDates } from "react-native-calendars";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";

LocaleConfig.locales['kr'] = {
  monthNames: [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월','10월','11월','12월'],
  monthNamesShort: ['1.', "2.", "3.", "4.", "5.", "6.", "7.", "8.", "9.", "10.", "11.", "12."],
  dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
  dayNamesShort: ['일', '월', '화', '수','목', '금','토'],
};

LocaleConfig.defaultLocale = 'kr';

export default function PurposeCreate2Screen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const [purposeInfo, setPurposeInfo] = useState(route.params.purposeInfo);
  const [selectedDates, setSelectedDates] = useState({
    startedAt: null,
    endAt: null,
  });
  const [isCalendarVisible, setCalendarVisible] = useState(false);
  const [settingDateType, setSettingDateType] = useState(""); // "start" 또는 "end"
  const [isValid, setIsValid] = useState(false);
  const handleDateChange = (day) => {
    if (!selectedDates.startedAt) {
      setSelectedDates({
        ...selectedDates,
        startedAt: day.dateString,
      });
    } else if (!selectedDates.endAt) {
      setSelectedDates({
        startedAt: selectedDates.startedAt,
        endAt: day.dateString,
      });
      setIsValid(true);
    }
  };
  const getDatesBetweenDates = (startedAt, endAt) => {
    let dates = [];
    const currDate = new Date(startedAt);
    const lastDate = new Date(endAt);
    while (currDate <= lastDate) {
      dates.push(currDate.toISOString().split("T")[0]);
      currDate.setDate(currDate.getDate() + 1);
    }
    return dates;
  };
  const markedDates = {
    ...(selectedDates.startedAt
      ? {
          [selectedDates.startedAt]: { selected: true, color: "blue" },
        }
      : {}),
    ...(selectedDates.endAt
      ? {
          [selectedDates.endAt]: { selected: true, color: "red" },
        }
      : {}),
    ...(purposeInfo.startedAt
      ? {
          [purposeInfo.startedAt]: { selected: true, color: "blue" },
        }
      : {}),
    ...getDatesBetweenDates(
      selectedDates.startedAt,
      selectedDates.endAt
    ).reduce((acc, date) => {
      acc[date] = { selected: true, color: "green" };
      return acc;
    }, {}),
    ...getDatesBetweenDates(purposeInfo.startedAt, purposeInfo.endAt).reduce(
      (acc, date) => {
        acc[date] = { selected: true, color: "green" };
        return acc;
      },
      {}
    ),
  };
  const handleConfirmDates = () => {
    setPurposeInfo({
      ...purposeInfo,
      startedAt: selectedDates.startedAt,
      endAt: selectedDates.endAt,
    });
    setSelectedDates({
      startedAt: null,
      endAt: null,
    });
    setCalendarVisible(false);
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="목표생성(2/3)"
        cancelNavi="PurposeScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>목표 기간 설정하기</Text>
          <Text style={styles.subtitle}>
            목표 시작 날짜와 종료 날짜를 선택해주세요.
          </Text>

          <Text style={styles.dateText}>시작 날짜</Text>
          <TouchableWithoutFeedback
            onPress={() => {
              setSettingDateType("start");
              setSelectedDates({
                startedAt: null,
                endAt: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                value={purposeInfo.startedAt}
                // multiline={true}
                editable={false}
              />
              <AntDesign name="calendar" size={24} color="black" />
            </View>
          </TouchableWithoutFeedback>

          <Text style={styles.dateText}>종료 날짜</Text>
          <TouchableWithoutFeedback
            onPress={() => {
              setSettingDateType("end");
              setSelectedDates({
                startedAt: null,
                endAt: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={[styles.input]}
                value={purposeInfo.endAt}
                // multiline={true}
                editable={false}
              />
              <AntDesign name="calendar" size={24} color="black" />
            </View>
          </TouchableWithoutFeedback>

          {/* 달력 모달 부분 */}
          {isCalendarVisible && (
            <View style={styles.centeredView}>
              <Modal
                // animationType="slide"
                animationType="none"
                transparent={true}
                visible={isCalendarVisible}
                onRequestClose={() => {
                  setCalendarVisible(false);
                }}
              >
                <View style={{ flex: 1, justifyContent: "center" }}>
                  <Calendar
                    onDayPress={(day) => handleDateChange(day)}
                    markedDates={markedDates}
                  />
                  <TouchableOpacity
                    style={styles.button}
                    onPress={()=>{
                      handleConfirmDates()
                    }}
                  >
                    <Text style={styles.buttonText}>다음</Text>
                  </TouchableOpacity>
                </View>
              </Modal>
            </View>

          )}
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={() =>
            navigation.navigate("PurposeCreate3Screen", {
              purposeInfo: purposeInfo,
            })
          }
          //TODO: 풀기
          // disabled={!isValid}
        >
          <Text style={styles.buttonText}>다음</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  innerContainer: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 60,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginTop: 10,
    marginBottom: 40,
    backgroundColor: "#D9D9D920",
    position: "relative",
  },
  input: {
    flex: 1,
    height: 20,
    padding: 0, // remove padding to avoid overlap
    fontSize: 16,
    // textAlign: "center",
    color:'black'
  },
  button: {
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  dateText: {
    fontSize: 18,
  },
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
});
