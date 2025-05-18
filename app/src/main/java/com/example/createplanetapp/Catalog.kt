package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Catalog : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        //Содержания спиннеров
        val tours_dropdown_content = arrayOf("Школьникам", "Семьям")
        val excursions_dropdown_content = arrayOf("По городу", "Пешеходные", "Загородные", "В музеях")
        val residence_dropdown_content = arrayOf("Отели", "Апарт-отели", "Апартаменты", "Хостелы")
        val services_dropdown_content = arrayOf("Подбор отеля", "Трансфер", "Аренда авто", "Катера и теплоходы", "Фотопрогулки", "Туристическое меню", "Корпоративный отдых")

        //Распаршенные экскурсии (нужно боооольше чем 2)
        val items_po_gorodu = csvParser(resources, R.raw.po_gorodu)
        val items_zagorodnie = csvParser(resources, R.raw.zagorodnie)

        //Лист экскурсий
        val recyclerView: RecyclerView = view.findViewById(R.id.catalog_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = null //Инициализация происходит при выборе опции

        //Спиннеры
        val spinner1: Spinner = view.findViewById(R.id.catalog_spinner_tours_options)
        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tours_dropdown_content)
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner1.adapter = adapter1

        val spinner2: Spinner = view.findViewById(R.id.catalog_spinner_excursions_options)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, excursions_dropdown_content)
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner2.adapter = adapter2

        val spinner3: Spinner = view.findViewById(R.id.catalog_spinner_residence_options)
        val adapter3 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, residence_dropdown_content)
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner3.adapter = adapter3

        val spinner4: Spinner = view.findViewById(R.id.catalog_spinner_services_options)
        val adapter4 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, services_dropdown_content)
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner4.adapter = adapter4


        //Кнопки из верхнего меню
        val b_tours: Button = view.findViewById(R.id.tours_button)
        val b_excursions: Button = view.findViewById(R.id.excursions_button)
        val b_residence: Button = view.findViewById(R.id.residence_button)
        val b_services: Button = view.findViewById(R.id.services_button)

        //Показать нужный spinner в зависимости от нажатой кнопки
        b_tours.setOnClickListener {
            spinner1.visibility = View.VISIBLE
            spinner2.visibility = View.GONE
            spinner3.visibility = View.GONE
            spinner4.visibility = View.GONE
        }
        b_excursions.setOnClickListener {
            spinner1.visibility = View.GONE
            spinner2.visibility = View.VISIBLE
            spinner3.visibility = View.GONE
            spinner4.visibility = View.GONE
        }
        b_residence.setOnClickListener {
            spinner1.visibility = View.GONE
            spinner2.visibility = View.GONE
            spinner3.visibility = View.VISIBLE
            spinner4.visibility = View.GONE
        }
        b_services.setOnClickListener {
            spinner1.visibility = View.GONE
            spinner2.visibility = View.GONE
            spinner3.visibility = View.GONE
            spinner4.visibility = View.VISIBLE
        }

        //Обработка выбора в спиннерах
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                recyclerView.adapter = when (position) {
                    else -> null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                recyclerView.adapter = when (position) {
                    0 -> createAdapter(items_po_gorodu)
                    2 -> createAdapter(items_zagorodnie)
                    else -> null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                recyclerView.adapter = when (position) {
                    else -> null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                recyclerView.adapter = when (position) {
                    else -> null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        return view
    }


    private fun createAdapter(items: List<ItemsViewModel>): CatalogAdapter {
        return CatalogAdapter(items, requireContext())
    }


}
